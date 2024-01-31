package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.goudong.authentication.common.core.*;
import com.goudong.authentication.common.util.HttpRequestUtil;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.constant.DateConst;
import com.goudong.authentication.server.constant.UserConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.properties.AuthenticationServerProperties;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户管理服务层接口实现类
 * @author chenf
 */
@Slf4j
@Service
public class BaseUserManagerServiceImpl implements BaseUserManagerService {
    //~fields
    //==================================================================================================================
    /**
     * 用户服务接口
     */
    @Resource
    private BaseUserService baseUserService;

    /**
     * 应用服务接口
     */
    @Resource
    private BaseAppService baseAppService;

    /**
     * 角色服务接口
     */
    @Resource
    private BaseRoleService baseRoleService;

    /**
     * 菜单服务接口
     */
    @Resource
    private BaseMenuService baseMenuService;

    /**
     * 密码编码器
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 认证服务配置
     */
    @Resource
    private AuthenticationServerProperties authenticationServerProperties;

    //~methods
    //==================================================================================================================
    /**
     * 根据应用Id和用户名查询用户
     * @param appId     应用Id
     * @param username  用户名
     * @return          用户信息
     */
    @Override
    public BaseUser findOneByAppIdAndUsername(Long appId, String username) {
        return baseUserService.findOneByAppIdAndUsername(appId, username);
    }

    /**
     * 获取登录成功信息
     * @param myAuthentication  用户认证成功对象
     * @return                  用户基本信息和token
     */
    @Override
    public LoginResp login(MyAuthentication myAuthentication) {
        LoginResp loginResp = new LoginResp();
        // 设置角色
        List<String> roles = myAuthentication.getRoles().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        loginResp.setId(myAuthentication.getId());
        loginResp.setUsername(myAuthentication.getUsername());
        loginResp.setAppId(myAuthentication.getAppId());
        loginResp.setRealAppId(myAuthentication.getRealAppId());
        loginResp.setRoles(roles);

        BaseApp app = baseAppService.findById(myAuthentication.getAppId());

        // 创建token
        AuthenticationServerProperties.TokenConfigInner tokenConfig = authenticationServerProperties.getToken();
        Jwt jwt = new Jwt(tokenConfig.getAccessTokenExpiration(), tokenConfig.getAccessTokenExpirationTimeUnit(), tokenConfig.getRefreshTokenExpiration(), tokenConfig.getRefreshTokenExpirationTimeUnit(), app.getSecret());
        UserSimple userSimple = new UserSimple(myAuthentication.getId(), myAuthentication.getAppId(), myAuthentication.getRealAppId(), myAuthentication.getUsername(), roles);
        Token token = jwt.generateToken(userSimple);
        loginResp.setToken(token);
        // 设置应用首页地址
        loginResp.setHomePage(app.getHomePage());
        log.info("认证成功，响应用户登录信息:{}", JsonUtil.toJsonString(loginResp));
        return loginResp;
    }

    /**
     * 刷新token
     * @param token refreshToken
     * @return      token对象
     */
    @Override
    public Token refreshToken(RefreshToken token) {
        Long xAppId = HttpRequestUtil.getXAppId();
        BaseApp app = baseAppService.findById(xAppId);
        AuthenticationServerProperties.TokenConfigInner tokenConfig = authenticationServerProperties.getToken();
        Jwt jwt = new Jwt(tokenConfig.getAccessTokenExpiration(), tokenConfig.getAccessTokenExpirationTimeUnit(), tokenConfig.getRefreshTokenExpiration(), tokenConfig.getRefreshTokenExpirationTimeUnit(), app.getSecret());
        UserSimple userSimple = jwt.parseToken(token.getRefreshToken());

        // 校验应用，应用未激活，应用下所有用户都不能使用
        BaseApp realApp = baseAppService.findById(userSimple.getRealAppId());
        AssertUtil.isTrue(realApp.getEnabled(), () -> new DisabledException("应用未激活"));
        return jwt.generateToken(userSimple);
    }

    /**
     * 根据{@code token}获取用户信息
     * @param token     token
     * @return          用户信息
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetail getUserDetailByToken(String token) {
        BaseApp baseApp = baseAppService.findByHeader();
        UserSimple userSimple = Jwt.parseToken(baseApp.getSecret(), token);

        // 校验应用，应用未激活，应用下所有用户都不能使用
        BaseApp realApp = baseAppService.findById(userSimple.getRealAppId());
        AssertUtil.isTrue(realApp.getEnabled(), () -> new DisabledException("应用未激活"));

        // token用户是否是超级管理员
        boolean isSuperAdmin = userSimple.superAdmin();
        boolean isAdmin = userSimple.admin();

        // 查询用户角色菜单
        BaseUser baseUser = baseUserService.findDetailById(userSimple.getId());

        UserDetail userDetail = new UserDetail();
        userDetail.setId(baseUser.getId());
        userDetail.setAppId(baseUser.getAppId());
        userDetail.setRealAppId(baseUser.getRealAppId());
        userDetail.setUsername(baseUser.getUsername());

        List<BaseRole> roles = baseUser.getRoles();
        List<String> roleNames = new ArrayList<>(roles.size());
        Set<Menu> menuHashSet = new HashSet<>(roles.size());
        roles.forEach(p -> {
            roleNames.add(p.getName());

            // 不是管理员(超级管理员)，使用本身拥有的菜单
            if (!isAdmin && !isSuperAdmin) {
                p.getMenus().forEach(p2 -> {
                    menuHashSet.add(BeanUtil.copyProperties(p2, Menu.class));
                });
            }
        });

        // 是超级管理员||管理员，查询应用下所有菜单
        if (isSuperAdmin) {
            List<Menu> menus = BeanUtil.copyToList(baseMenuService.findAllByAppId(baseUser.getAppId()), Menu.class, CopyOptions.create());
            menuHashSet.addAll(menus);
        } else if (isAdmin) {    // 管理员，查询应用下所有菜单
            // 查询管理后台菜单
            List<BaseMenu> menus1 = baseMenuService.findAllByAppId(baseUser.getAppId());
            // 排除某些只能超级管理员才能拥有的权限
            menus1 = menus1.stream().filter(f -> {
                boolean flag = true;
                for (Long im : CommonConst.ROLE_APP_ADMIN_IGNORE_MENUS) {
                    // 菜单
                    if (Objects.equals(f.getId(), im) || Objects.equals(f.getParentId(), im)) {
                        flag = false;
                        break;
                    }
                }
                return flag;
            }).collect(Collectors.toList());

            // 查询自己应用的所有菜单
            if (!Objects.equals(baseUser.getAppId(), baseUser.getRealAppId())) {
                List<BaseMenu> menus2 = baseMenuService.findAllByAppId(baseUser.getRealAppId());
                menus1.addAll(menus2);
            }

            List<Menu> menus = BeanUtil.copyToList(menus1, Menu.class, CopyOptions.create());
            menuHashSet.addAll(menus);
        }

        List<Menu> menuArrayList = new ArrayList<>(menuHashSet);
        menuArrayList.sort(new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return Optional.ofNullable(o1.getSortNum()).orElseGet(() ->Integer.MAX_VALUE).compareTo(Optional.ofNullable(o2.getSortNum()).orElseGet(() ->Integer.MAX_VALUE));
            }
        });
        // 处理菜单
        userDetail.setRoles(roleNames);
        userDetail.setMenus(menuArrayList);

        return userDetail;
    }

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     * @param req   请求参数
     * @return      用户下拉列表
     */
    @Override
    public PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDownReq req) {
        return baseUserService.userDropDown(req);
    }

    /**
     * 分页查询用户
     * @param req   分页参数
     * @return      用户分页对象
     */
    @Override
    public PageResult<BaseUserPageResp> page(BaseUserPageReq req) {
        return baseUserService.page(req);
    }

    /**
     * 简单方式创建用户
     * @param req   用户信息
     * @return      用户对象
     */
    @Override
    public BaseUserDTO simpleCreateUser(BaseUserSimpleCreateReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();

        BaseUser user = new BaseUser();
        user.setAppId(realAppId);
        user.setRealAppId(realAppId);
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user.setLocked(false);
        user.setValidTime(DateConst.MAX_DATE_TIME);
        user.setRemark(req.getRemark());
        // 设置角色
        user.setRoles(baseRoleService.listByIds(req.getRoleIds()));

        return baseUserService.save(user);
    }

    /**
     * 简单方式修改用户
     * @param req   修改用户参数
     * @return      用户信息
     */
    @Override
    public BaseUserDTO simpleUpdateUser(BaseUserSimpleUpdateReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        BaseUser user = baseUserService.findById(req.getId());
        AssertUtil.isEquals(realAppId, user.getRealAppId(), () -> ClientException.clientByForbidden().serverMessage("不能修改其它应用下的用户"));
        if (CollectionUtil.isNotEmpty(req.getRoleIds())) {
            List<BaseRole> baseRoles = baseRoleService.listByIds(req.getRoleIds());
            baseRoles.forEach(p -> AssertUtil.isEquals(realAppId, p.getAppId(), () -> ClientException.clientByForbidden().serverMessage("不能使用其它应用下的角色")));
            user.setRoles(baseRoles);
        }

        user.setEnabled(Optional.ofNullable(req.getEnabled()).orElseGet(user::getEnabled));
        user.setLocked(Optional.ofNullable(req.getLocked()).orElseGet(user::getLocked));
        user.setValidTime(Optional.ofNullable(req.getValidTime()).orElseGet(user::getValidTime));
        user.setRemark(Optional.ofNullable(req.getRemark()).orElseGet(user::getRemark));

        return baseUserService.save(user);
    }

    /**
     * 批量删除用户
     * @param ids   被删除的用户id集合
     * @return      true:删除成功；false:删除失败
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        return baseUserService.deleteByIds(ids);
    }

    /**
     * 重置用户密码
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    @Override
    public Boolean resetPassword(Long userId) {
        return baseUserService.resetPassword(userId);
    }

    /**
     * 修改用户激活状态
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeEnabled(Long userId) {
        return baseUserService.changeEnabled(userId);
    }

    /**
     * 修改用户锁定状态
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    @Override
    public Boolean changeLocked(Long userId) {
        return baseUserService.changeLocked(userId);
    }

    /**
     * 补充token信息
     * @param req 填充的内容
     * @return  填充后新生成的token
     */
    @Override
    public Token supplementToken(Map<String, Object> req) {
        // 获取userSimple对象
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        UserSimple userSimple = myAuthentication.convertUserSimple();
        userSimple.setDetail(req);

        // 构造新的token
        Long xAppId = HttpRequestUtil.getXAppId();
        BaseApp app = baseAppService.findById(xAppId);
        AuthenticationServerProperties.TokenConfigInner tokenConfig = authenticationServerProperties.getToken();
        Jwt jwt = new Jwt(tokenConfig.getAccessTokenExpiration(), tokenConfig.getAccessTokenExpirationTimeUnit(), tokenConfig.getRefreshTokenExpiration(), tokenConfig.getRefreshTokenExpirationTimeUnit(), app.getSecret());
        return jwt.generateToken(userSimple);
    }
}
