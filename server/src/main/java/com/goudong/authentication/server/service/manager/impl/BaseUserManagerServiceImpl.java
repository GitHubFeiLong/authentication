package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.authentication.common.core.*;
import com.goudong.authentication.common.util.*;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.constant.DateConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.exception.BasicException;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.properties.AuthenticationServerProperties;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.req.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.authentication.server.util.HttpRequestUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
        Long appId = HttpRequestUtil.getXAppId();
        log.info("使用请求头中的appId {} 创建token", appId);
        BaseApp app = baseAppService.findById(appId);
        // 设置应用首页地址
        loginResp.setHomePage(app.getHomePage());
        loginResp.setAppName(app.getName());
        loginResp.setRealHomePage(app.getHomePage());
        loginResp.setRealAppName(app.getName());
        // 不相同就需要查询
        if (!Objects.equals(myAuthentication.getAppId(), myAuthentication.getRealAppId())) {
            BaseApp realApp = baseAppService.findById(myAuthentication.getRealAppId());
            loginResp.setRealHomePage(realApp.getHomePage());
            loginResp.setRealAppName(realApp.getName());
        }

        // 创建token
        AuthenticationServerProperties.TokenConfigInner tokenConfig = authenticationServerProperties.getToken();
        Jwt jwt = new Jwt(tokenConfig.getAccessTokenExpiration(), tokenConfig.getAccessTokenExpirationTimeUnit(), tokenConfig.getRefreshTokenExpiration(), tokenConfig.getRefreshTokenExpirationTimeUnit(), app.getSecret());
        UserSimple userSimple = new UserSimple(myAuthentication.getId(), myAuthentication.getAppId(), myAuthentication.getRealAppId(), myAuthentication.getUsername(), roles);
        Token token = jwt.generateToken(userSimple);
        loginResp.setToken(token);
        LogUtil.info(log, "更新用户最近登录时间");
        int updateCount = baseUserService.updateLastLoginTime(new Date(), myAuthentication.getId());

        log.info("认证成功，响应用户登录信息:{}", JsonUtil.toJsonString(loginResp));
        return loginResp;
    }

    /**
     * 刷新token
     * @param token refreshToken
     * @return      token对象
     */
    @Override
    public Token refreshToken(BaseUserRefreshTokenReq token) {
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
        userDetail.setRealAppName(realApp.getName());
        userDetail.setRealAppHome(realApp.getHomePage());
        userDetail.setUsername(baseUser.getUsername());

        List<BaseRole> roles = baseUser.getRoles();
        List<String> roleNames = new ArrayList<>(roles.size());
        Set<Menu> menuHashSet = new HashSet<>(roles.size());
        roles.forEach(p -> {
            roleNames.add(p.getName());

            // 不是管理员(超级管理员)，使用本身拥有的菜单
            if (!isAdmin) {
                p.getMenus().forEach(p2 -> {
                    menuHashSet.add(BeanUtil.copyProperties(p2, Menu.class));
                });
            }
        });

        // 是超级管理员||管理员，查询应用下所有菜单
        if (isSuperAdmin) {
            log.info("用户是超级管理员，查询认证服务下的所有菜单");
            List<Menu> menus = BeanUtil.copyToList(baseMenuService.findAllByAppId(CommonConst.AUTHENTICATION_SERVER_APP_ID), Menu.class, CopyOptions.create());
            menuHashSet.addAll(menus);
        } else if (isAdmin) {    // 管理员，查询应用下所有菜单
            if (userSimple.sysUser()) {
                log.info("用户是认证服务的管理员，查询认证服务的下的所有菜单");
                // 查询管理后台菜单
                List<BaseMenu> menus1 = baseMenuService.findAllByAppId(CommonConst.AUTHENTICATION_SERVER_APP_ID);
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
                List<Menu> menus = BeanUtil.copyToList(menus1, Menu.class, CopyOptions.create());
                menuHashSet.addAll(menus);
            } else {
                log.info("用户是管理员，查询自己应用下的所有菜单");
                // 查询自己应用的所有菜单
                List<BaseMenu> menus2 = baseMenuService.findAllByAppId(baseUser.getRealAppId());

                List<Menu> menus = BeanUtil.copyToList(menus2, Menu.class, CopyOptions.create());
                menuHashSet.addAll(menus);
            }
        }

        List<Menu> menuArrayList = new ArrayList<>(menuHashSet);
        // 进行菜单排序
        log.info("将菜单进行排序");
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
        String password = StringUtil.isNotBlank(req.getPassword()) ? req.getPassword() : authenticationServerProperties.getApp().getUserDefaultPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setLocked(false);
        user.setValidTime(DateConst.MAX_DATE_TIME);
        user.setRemark(req.getRemark());
        // 设置角色
        user.setRoles(baseRoleService.listByIds(req.getRoleIds()));
        user.setLastLoginTime(DateConst.MIN_DATE_TIME);

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
     *
     * @param req 填充的内容
     * @return  填充后新生成的token
     */
    @Override
    public Token supplementToken(BaseUserSupplementTokenReq req) {
        // 构造新的token
        Long xAppId = HttpRequestUtil.getXAppId();
        BaseApp app = baseAppService.findById(xAppId);
        // 获取userSimple对象
        UserSimple userSimple = Jwt.parseToken(app.getSecret(), req.getToken());
        userSimple.getDetail().putAll(req.getDetail());

        AuthenticationServerProperties.TokenConfigInner tokenConfig = authenticationServerProperties.getToken();
        Jwt jwt = new Jwt(tokenConfig.getAccessTokenExpiration(), tokenConfig.getAccessTokenExpirationTimeUnit(), tokenConfig.getRefreshTokenExpiration(), tokenConfig.getRefreshTokenExpirationTimeUnit(), app.getSecret());
        return jwt.generateToken(userSimple);
    }

    /**
     * 修改用户密码
     *
     * @param req 前端请求参数
     * @return true:修改成功；false：修改失败
     */
    @Override
    public Boolean changePassword(BaseUserChangePasswordReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        BaseUser user = baseUserService.findById(myAuthentication.getId());
        AssertUtil.isNotNull(user, () -> BasicException.client("用户不存在"));
        // 验证旧密码
        boolean passwordMatches = CommonConst.BCRYPT_PATTERN.matcher(req.getOldPassword()).matches()
                // 是密码格式，直接比较值
                ? Objects.equals(req.getOldPassword(), user.getPassword())
                // 使用 BCrypt 加密的方式进行匹配
                : passwordEncoder.matches(req.getOldPassword(), user.getPassword());
        AssertUtil.isTrue(passwordMatches, () -> BasicException.client("当前密码不正确"));
        log.info("用户输入的旧密码匹配成功");
        // 设置新密码
        if (CommonConst.BCRYPT_PATTERN.matcher(req.getNewPassword()).matches()) {
            log.info("新密码是BCrypt格式的字符串，直接设置成密码：{}", req.getNewPassword());
            user.setPassword(req.getNewPassword());
        } else {
            log.info("加密密码：{}", req.getNewPassword());
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }
        baseUserService.save(user);
        return true;
    }

    /**
     * 给指定用户创建token
     *
     * @param req 请求对象
     * @return token
     */
    @Override
    public Token createToken(BaseUserCreateTokenReq req) {
        // 构造新的token
        Long xAppId = HttpRequestUtil.getXAppId();
        BaseApp app = baseAppService.findById(xAppId);
        AssertUtil.isTrue(app.getEnabled(), () -> new DisabledException("应用未激活"));
        // 查询用户信息
        BaseUser user = Optional.ofNullable(baseUserService.findOneByRealAppIdAndUsername(xAppId, req.getUsername())).orElseThrow(() -> ClientException.client("用户不存在"));
        // 校验用户状态
        AssertUtil.isTrue(user.getEnabled(), () -> {
            log.warn("选择了应用,用户未激活");
            return new DisabledException("用户未激活");
        });

        AssertUtil.isFalse(user.getLocked(), () -> {
            log.warn("根据X-App-Id查询用户，用户已锁定");
            return new LockedException("用户已锁定");
        });

        AssertUtil.isTrue(user.getValidTime().after(new Date()), () -> {
            log.warn("选择了应用,账户已过期");
            return new AccountExpiredException("账户已过期");
        });
        List<String> roleNames = user.getRoles().stream().map(BaseRole::getName).collect(Collectors.toList());
        // 获取userSimple对象
        UserSimple userSimple = new UserSimple(user.getId(), user.getAppId(), user.getRealAppId(), user.getUsername(), roleNames);

        AuthenticationServerProperties.TokenConfigInner tokenConfig = authenticationServerProperties.getToken();
        Jwt jwt = new Jwt(tokenConfig.getAccessTokenExpiration(), tokenConfig.getAccessTokenExpirationTimeUnit(), tokenConfig.getRefreshTokenExpiration(), tokenConfig.getRefreshTokenExpirationTimeUnit(), app.getSecret());
        return jwt.generateToken(userSimple);
    }
}
