package com.goudong.authentication.server.config.security;

import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.exception.ClientExceptionEnum;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 自定义认证处理
 * @author chenf
 */
@Slf4j
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    /**
     * 密码编码器
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 用户管理服务接口
     */
    @Resource
    private BaseUserManagerService baseUserManagerService;

    /**
     * 应用管理服务接口
     */
    @Resource
    private BaseAppManagerService baseAppManagerService;

    /**
     * 自定义登录认证
     * @param authentication            前端传递的认证参数，包含用户名密码
     * @see UsernameNotFoundException   用户未找到异常
     * @see AccountExpiredException     账户失效异常
     * @see BadCredentialsException     密码错误异常
     * @return 认证成功对象
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        /*
            获取登录参数
         */
        // 表单输入的用户名
        String username = (String) authentication.getPrincipal();
        // 表单输入的密码
        String password = (String) authentication.getCredentials();
        AssertUtil.isFalse(StringUtils.isBlank(username) || StringUtils.isBlank(password), () -> ClientException.client(ClientExceptionEnum.BAD_REQUEST, "请输入正确的用户名和密码"));

        // 获取其它参数
        WebAuthenticationDetailsImpl webAuthenticationDetails = (WebAuthenticationDetailsImpl) authentication.getDetails();
        // 用户选择的应用id
        Long selectAppId = webAuthenticationDetails.getSelectAppId();
        // 客户端请求头上的应用id
        Long xAppId = webAuthenticationDetails.getXAppId();
        AssertUtil.isNotNull(xAppId, () -> ClientException.client(ClientExceptionEnum.BAD_REQUEST, "请求头X-App-Id丢失"));
        log.debug("登录参数如下，username={},password={},selectAppId={},xAppId={}", username, password, selectAppId, xAppId);
        //======== 登录逻辑
        // 选择应用id登录逻辑
        if (selectAppId != null) {
            return selectAppIdAuthentication(selectAppId, username, password);
        }

        // 请求头应用id登录逻辑
        return xAppIdAuthentication(xAppId, username, password);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    /**
     * 选择应用id进行认证
     * @param selectAppId   所选应用id
     * @param username      用户名
     * @param password      密码
     * @return 认证成功时，返回认证成功对象
     */
    public MyAuthentication selectAppIdAuthentication(Long selectAppId, String username, String password) {
        log.info("选择了应用：{}", selectAppId);
        // 校验应用，应用未激活，应用下所有用户都不能使用
        BaseApp app = baseAppManagerService.findById(selectAppId);
        AssertUtil.isTrue(app.getEnabled(), () -> new DisabledException("应用未激活"));

        BaseUser user = Optional.ofNullable(baseUserManagerService.findOneByAppIdAndUsername(selectAppId, username))
                .orElseGet(() -> baseUserManagerService.findOneByAppIdAndUsername(CommonConst.AUTHENTICATION_SERVER_APP_ID, username));

        AssertUtil.isNotNull(user, () -> {
            log.warn("选择了应用,用户名不存在");
            return new UsernameNotFoundException("用户不存在");
        });
        // 所选用户存在
        log.info("选择了应用,并且用户存在");
        boolean passwordMatches = CommonConst.BCRYPT_PATTERN.matcher(password).matches()
                // 是密码格式，直接比较值
                ? Objects.equals(password, user.getPassword())
                // 使用 BCrypt 加密的方式进行匹配
                : passwordEncoder.matches(password, user.getPassword());
        // 用户身份信息校验
        // 密码校验
        AssertUtil.isTrue(passwordMatches, () -> {
            log.warn("选择了应用,用户密码错误");
            return new BadCredentialsException("用户密码错误");
        });

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

        // 用户校验通过，选择应用（管理员和应用下普通用户）
        log.info("选择了应用,用户校验成功");
        MyAuthentication myAuthentication = new MyAuthentication();
        myAuthentication.setId(user.getId());
        myAuthentication.setAppId(user.getAppId());
        myAuthentication.setRealAppId(user.getRealAppId());
        myAuthentication.setUsername(user.getUsername());
        List<SimpleGrantedAuthority> roles = user.getRoles().stream().map(m -> new SimpleGrantedAuthority(m.getName())).collect(Collectors.toList());
        myAuthentication.setRoles(roles);
        return myAuthentication;
    }

    /**
     * 选择应用id进行认证，
     * @param xAppId    应用id
     * @param username  用户名
     * @param password  密码
     * @return 认证成功时，返回认证成功对象
     */
    @Transactional
    public MyAuthentication xAppIdAuthentication(Long xAppId, String username, String password) {
        log.info("开始根据X-App-Id:{} 校验用户:{}", xAppId, username);
        // 校验应用，应用未激活，应用下所有用户都不能使用
        BaseApp app = baseAppManagerService.findById(xAppId);
        AssertUtil.isTrue(app.getEnabled(), () -> new DisabledException("应用未激活"));

        // 未选择应用,或者选择的应用校验用户失败
        BaseUser user = baseUserManagerService.findOneByAppIdAndUsername(xAppId, username);
        AssertUtil.isNotNull(user, () -> {
            log.warn("选择了应用,用户名不存在");
            return new UsernameNotFoundException("用户不存在");
        });
        // 所选用户存在
        log.info("根据X-App-Id查询用户，用户存在");
        boolean passwordMatches = CommonConst.BCRYPT_PATTERN.matcher(password).matches()
                // 是密码格式，直接比较值
                ? Objects.equals(password, user.getPassword())
                // 使用 BCrypt 加密的方式进行匹配
                : passwordEncoder.matches(password, user.getPassword());

        // 用户身份信息校验
        // 密码校验
        AssertUtil.isTrue(passwordMatches, () -> {
            log.warn("根据X-App-Id查询用户，用户密码错误");
            return new BadCredentialsException("用户密码错误");
        });

        AssertUtil.isTrue(user.getEnabled(), () -> {
            log.warn("根据X-App-Id查询用户，用户未激活");
            return new DisabledException("用户未激活");
        });

        AssertUtil.isFalse(user.getLocked(), () -> {
            log.warn("根据X-App-Id查询用户，用户已锁定");
            return new LockedException("用户已锁定");
        });

        AssertUtil.isTrue(user.getValidTime().after(new Date()), () -> {
            log.warn("根据X-App-Id查询用户，账户已过期");
            return new AccountExpiredException("账户已过期");
        });

        // 未选择应用
        log.info("登录成功：未选择应用，用户校验成功");
        MyAuthentication myAuthentication = new MyAuthentication();
        myAuthentication.setId(user.getId());
        myAuthentication.setAppId(user.getAppId());
        myAuthentication.setRealAppId(user.getRealAppId());
        myAuthentication.setUsername(user.getUsername());
        List<SimpleGrantedAuthority> roles = user.getRoles().stream().map(m -> new SimpleGrantedAuthority(m.getName())).collect(Collectors.toList());
        myAuthentication.setRoles(roles);
        return myAuthentication;
    }
}
