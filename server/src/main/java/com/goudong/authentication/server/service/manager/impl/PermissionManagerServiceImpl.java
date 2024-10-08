package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.common.core.AuthorizationContext;
import com.goudong.authentication.common.core.Jwt;
import com.goudong.authentication.common.core.UserSimple;
import com.goudong.authentication.common.security.cer.CertificateUtil;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.server.domain.*;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.exception.ClientExceptionEnum;
import com.goudong.authentication.server.properties.AuthenticationServerProperties;
import com.goudong.authentication.server.rest.req.CheckPermissionReq;
import com.goudong.authentication.server.rest.req.PermissionGetUserReq;
import com.goudong.authentication.server.rest.req.PermissionListPermissionByUsernameReq;
import com.goudong.authentication.server.rest.resp.PermissionGetMenusResp;
import com.goudong.authentication.server.rest.resp.PermissionGetRolesMenusResp;
import com.goudong.authentication.server.rest.resp.PermissionGetUserResp;
import com.goudong.authentication.server.rest.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.dto.PermissionDTO;
import com.goudong.authentication.server.service.manager.PermissionManagerService;
import com.goudong.authentication.server.util.HttpRequestUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 权限管理服务
 * @author chenf
 * @version 1.0
 */
@Slf4j
@Service
public class PermissionManagerServiceImpl implements PermissionManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppService baseAppService;

    @Resource
    private BaseMenuService baseMenuService;

    @Resource
    private BaseUserService baseUserService;

    @Resource
    private BaseRoleService baseRoleService;

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AuthenticationServerProperties authenticationServerProperties;

    //~methods
    //==================================================================================================================
    /**
     * 获取应用下的所有菜单
     * @return  应用所有菜单
     */
    @Override
    public PermissionGetMenusResp getMenus() {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        PermissionGetMenusResp resp = new PermissionGetMenusResp();
        // 查询应用下的所有菜单
        List<BaseMenu> menus = baseMenuService.findAllByAppId(realAppId);
        resp.setMenus(BeanUtil.copyToList(menus, PermissionGetMenusResp.MenuInner.class));
        return resp;
    }

    /**
     * 获取角色和角色拥有的权限
     *
     * @return 角色集合
     */
    @Override
    @Transactional(readOnly = true)
    public PermissionGetRolesMenusResp getRolesMenus() {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        // 查询应用下的角色
        List<BaseRole> baseRoles = baseRoleService.listByAppId(realAppId);

        // 针对管理员角色进行填充菜单信息
        AtomicReference<List<BaseMenu>> adminMenus = new AtomicReference<>(null);
        baseRoles.forEach(role -> {
            // 管理员，需要查询应用下的所有菜单
            if (role.admin() || role.superAdmin()) {
                if (adminMenus.get() == null) {
                    adminMenus.set(baseMenuService.findAllByAppId(realAppId));
                }
                role.setMenus(adminMenus.get());
            }
        });
        PermissionGetRolesMenusResp resp = new PermissionGetRolesMenusResp();
        resp.setRoles(BeanUtil.copyToList(baseRoles, PermissionGetRolesMenusResp.RoleInner.class));
        return resp;
    }

    /**
     * 获取用户的信息
     *
     * @param req 用户名等参数
     * @return 用户信息
     */
    @Override
    @Transactional(readOnly = true)
    public PermissionGetUserResp getUser(PermissionGetUserReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        // 查询用户
        BaseUser baseUser = Optional.ofNullable(baseUserService.findOneByRealAppIdAndUsername(realAppId, req.getUsername()))
                .orElseThrow(() -> ClientException.client(ClientExceptionEnum.NOT_FOUND, "用户不存在"));
        PermissionGetUserResp resp = BeanUtil.copyProperties(baseUser, PermissionGetUserResp.class);
        return resp;
    }


    /**
     * 查询权限列表
     *
     * @return 权限列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> listPermission() {
        String authorization = Optional.ofNullable(httpServletRequest.getHeader("Authorization"))
                .orElseThrow(() -> ClientException.client("请求头Authorization丢失"));

        // 校验请求头
        if (authorization.startsWith("Bearer ")) {
            Long xAppId = HttpRequestUtil.getXAppId();
            MyAuthentication myAuthentication = SecurityContextUtil.get();
            return baseMenuService.findAllPermission(xAppId);
        }

        if (authorization.startsWith("GOUDONG-SHA256withRSA")) {
            AuthorizationContext authorizationContext = AuthorizationContext.get(authorization);

            Long appId =  authorizationContext.getAppid();                  // 应用id
            String serialNumber =  authorizationContext.getSerialNumber();  // 证书序列号
            long timestamp =  authorizationContext.getTimestamp();          // 时间戳
            String nonceStr =  authorizationContext.getNonceStr();          // 随机字符串
            String signature =  authorizationContext.getSignature();        // 签名

            // 查询应用
            BaseApp baseApp = Optional.ofNullable(baseAppService.getById(appId)).orElseThrow(() -> ClientException.client("应用不存在"));
            AssertUtil.isTrue(Boolean.TRUE.equals(baseApp.getEnabled()), () -> ClientException.client("应用未激活"));
            // 查询应用证书
            BaseAppCert baseAppCert = baseApp.getCerts().stream().filter(f -> f.getSerialNumber().equals(serialNumber)).findFirst().orElseThrow(() -> ClientException.client("证书不存在"));
            AssertUtil.isTrue(baseAppCert.getValidTime().after(new Date()), () -> ClientException.client("证书已过期"));

            // 验签
            // 签名校验
            String body = HttpRequestUtil.getBody(httpServletRequest);
            // 拼装消息 // 转换byte[]
            byte[] message = (appId + "\n" + serialNumber + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n").getBytes(StandardCharsets.UTF_8);
            try {
                // 加载证书
                X509Certificate certificate = CertificateUtil.loadCertificate(baseAppCert.getCert());
                Signature sign = Signature.getInstance("SHA256withRSA");
                sign.initVerify(certificate);
                sign.update(message);
                byte[] signatureB = Base64.decodeBase64(signature); // 拉卡拉返回的签名转byte[]
                boolean verify = sign.verify(signatureB); // 验签,验拉卡拉返回的签名

                log.info("拉卡拉验证签名结束，验证结果：{}", verify);
                AssertUtil.isTrue(verify, () -> ClientException.client("验签失败"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("当前java环境不支持SHA256withRSA", e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException("无效的证书", e);
            } catch (SignatureException e) {
                throw new RuntimeException("签名验证过程发生了错误", e);
            }

            // 签名验证成功，查询权限
            return baseMenuService.findAllPermission(appId);

        }

        throw ClientException.client("未知的认证方式");
    }

    /**
     * 检查是否有权限
     *
     * @return
     */
    @Override
    public Boolean checkPermission(){
        String authorization = Optional.ofNullable(httpServletRequest.getHeader("Authorization")).orElseThrow(() -> ClientException.client("请求头Authorization丢失"));

        // 校验请求头
        if (authorization.startsWith("Bearer ")) {
            MyAuthentication myAuthentication = SecurityContextUtil.get();
            List<String> userRoles = myAuthentication.getRoles().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());

            // 签名校验
            String body = HttpRequestUtil.getBody(httpServletRequest);
            try {
                // json转对象
                CheckPermissionReq req = objectMapper.readValue(body, CheckPermissionReq.class);
                AssertUtil.isNotBlank(req.getUri(), () -> ClientException.client("uri参数丢失"));
                AssertUtil.isNotBlank(req.getMethod(), () -> ClientException.client("method参数丢失"));
                AssertUtil.isNotBlank(req.getToken(), () -> ClientException.client("token参数丢失"));

                boolean check = check(myAuthentication.getAppId(), req.getUri(), req.getMethod(), userRoles);
                if (check) {
                    return true;
                }

                throw ClientException.clientByForbidden();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


        if (authorization.startsWith("GOUDONG-SHA256withRSA")) {
            AuthorizationContext authorizationContext = AuthorizationContext.get(authorization);

            Long appId =  authorizationContext.getAppid();                  // 应用id
            String serialNumber =  authorizationContext.getSerialNumber();  // 证书序列号
            long timestamp =  authorizationContext.getTimestamp();          // 时间戳
            String nonceStr =  authorizationContext.getNonceStr();          // 随机字符串
            String signature =  authorizationContext.getSignature();        // 签名

            // 查询应用
            BaseApp baseApp = Optional.ofNullable(baseAppService.getById(appId)).orElseThrow(() -> ClientException.client("应用不存在"));
            AssertUtil.isTrue(Boolean.TRUE.equals(baseApp.getEnabled()), () -> ClientException.client("应用未激活"));
            // 查询应用证书
            BaseAppCert baseAppCert = baseApp.getCerts().stream().filter(f -> f.getSerialNumber().equals(serialNumber)).findFirst().orElseThrow(() -> ClientException.client("证书不存在"));
            AssertUtil.isTrue(baseAppCert.getValidTime().after(new Date()), () -> ClientException.client("证书已过期"));

            // 验签
            // 签名校验
            String body = HttpRequestUtil.getBody(httpServletRequest);
            // 拼装消息 // 转换byte[]
            byte[] message = (appId + "\n" + serialNumber + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n").getBytes(StandardCharsets.UTF_8);
            try {
                // 加载证书
                X509Certificate certificate = CertificateUtil.loadCertificate(baseAppCert.getCert());
                Signature sign = Signature.getInstance("SHA256withRSA");
                sign.initVerify(certificate);
                sign.update(message);
                byte[] signatureB = Base64.decodeBase64(signature); // 拉卡拉返回的签名转byte[]
                boolean verify = sign.verify(signatureB); // 验签,验拉卡拉返回的签名

                log.info("拉卡拉验证签名结束，验证结果：{}", verify);
                AssertUtil.isTrue(verify, () -> ClientException.client("验签失败"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("当前java环境不支持SHA256withRSA", e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException("无效的证书", e);
            } catch (SignatureException e) {
                throw new RuntimeException("签名验证过程发生了错误", e);
            }

            try {
                // json转对象
                CheckPermissionReq req = objectMapper.readValue(body, CheckPermissionReq.class);
                AssertUtil.isNotBlank(req.getUri(), () -> ClientException.client("uri参数丢失"));
                AssertUtil.isNotBlank(req.getMethod(), () -> ClientException.client("method参数丢失"));
                AssertUtil.isNotBlank(req.getToken(), () -> ClientException.client("token参数丢失"));
                UserSimple userSimple = Jwt.parseToken(baseApp.getSecret(), req.getToken());
                List<String> userRoles = userSimple.getRoles();

                boolean check = check(appId, req.getUri(), req.getMethod(), userRoles);
                if (check) {
                    return true;
                }

                throw ClientException.clientByForbidden();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

        throw ClientException.client("未知的认证方式");
    }

    /**
     * 根据用户名获取他拥有的所有权限
     *
     * @param req 获取用户权限的参数
     * @return  用户拥有的所有角色和权限
     */
    @Override
    @Transactional
    public PermissionListPermissionByUsername2SimpleResp listPermissionByUsername2Simple(PermissionListPermissionByUsernameReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        BaseUser baseUser = baseUserService.findOneByRealAppIdAndUsername(realAppId, req.getUsername());
        AssertUtil.isNotNull(baseUser, () -> ClientException.client("用户不存在"));
        // 构造响应对象
        PermissionListPermissionByUsername2SimpleResp resp = new PermissionListPermissionByUsername2SimpleResp();
        List<PermissionListPermissionByUsername2SimpleResp.RoleInner> roleInners = new ArrayList<>(baseUser.getRoles().size());
        resp.setRoles(roleInners);
        if (CollectionUtil.isNotEmpty(baseUser.getRoles())) {
            // 循环角色，查询角色对应的权限
            baseUser.getRoles().forEach(role -> {
                PermissionListPermissionByUsername2SimpleResp.RoleInner roleInner = new PermissionListPermissionByUsername2SimpleResp.RoleInner();
                roleInners.add(roleInner);
                roleInner.setName(role.getName());

                List<BaseMenu> menus;
                if (role.superAdmin() || role.admin()) {
                    log.info("角色id：{}，角色名称：{} 是管理员，需要查询应用下所有菜单", role.getId(), role.getName());
                    menus = baseMenuService.findAllByAppId(role.getAppId());
                } else {
                    menus = role.getMenus();
                }
                // 将角色和菜单返回
                List<PermissionListPermissionByUsername2SimpleResp.MenuInner> menuInners = BeanUtil.copyToList(menus, PermissionListPermissionByUsername2SimpleResp.MenuInner.class);
                roleInner.setMenus(menuInners);
            });
        }
        return resp;
    }

    /**
     * 检查是否能访问
     * @param appId 应用id
     * @param uri 资源uri
     * @param method 请求方式
     * @param userRoles 用户角色
     * @return true有权限；false没权限
     */
    private boolean check(Long appId, String uri, String method, List<String> userRoles) {
        // 查询应用菜单权限
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> permissionRoles = baseMenuService.findAllPermission(appId).parallelStream().filter(f -> {
                    if (f.getType() == 2 || f.getType() == 3) { // 按钮和接口
                        return antPathMatcher.match(f.getPath(), uri) && f.getMethod().toUpperCase().contains(method.toUpperCase());
                    }
                    return false;
                })
                // 扁平化角色
                .flatMap(m -> m.getRoles().stream())
                .collect(Collectors.toList());

        log.info("url={}，method={}，用户权限={}，需要权限={}", uri, method, userRoles, permissionRoles);
        if (CollectionUtil.isEmpty(permissionRoles)) {
            return true;
        }

        return permissionRoles.stream().anyMatch(userRoles::contains);
    }
}
