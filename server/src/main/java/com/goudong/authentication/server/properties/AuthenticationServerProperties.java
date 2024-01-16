package com.goudong.authentication.server.properties;

import com.goudong.authentication.server.constant.UserConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 认证服务的配置类
 * @author chenf
 */
@Data
@ConfigurationProperties(prefix = "authentication.server")
public class AuthenticationServerProperties {
    //~fields
    //==================================================================================================================
    /**
     * app配置
     */
    @NestedConfigurationProperty
    private AppConfigInner app = new AppConfigInner();

    /**
     * 登录成功令牌配置
     */
    @NestedConfigurationProperty
    private TokenConfigInner token = new TokenConfigInner();

    //~methods
    //==================================================================================================================

    /**
     * 类描述：
     * app配置内部类
     * @author chenf
     */
    @Data
    public static class AppConfigInner {
        /**
         * 管理员的默认密码：
         * <ol>
         *     <li>新建应用管理员时，使用该配置作为密码</li>
         *     <li>重置应用管理员密码时，使用该配置作为密码</li>
         * </ol>
         */
        private String adminDefaultPassword = UserConst.ADMIN_DEFAULT_PASSWORD;

        /**
         * 用户的默认密码：
         * <ol>
         *     <li>导入用户未填写密码时，使用该配置作为密码</li>
         *     <li>重置用户密码时，使用该配置作为密码</li>
         * </ol>
         */
        private String userDefaultPassword = UserConst.USER_DEFAULT_PASSWORD;

        /**
         * 证书配置
         */
        @NestedConfigurationProperty
        private CertConfigInner cert = new CertConfigInner();
    }

    /**
     * 类描述：
     * 证书配置
     * @author chenf
     */
    @Data
    public static class CertConfigInner {

        /**
         * 证书颁发者
         */
        private String issuer = "goudong";

        /**
         * 证书有效时间
         */
        private Long validTime = 365 * 10L;

        /**
         * 证书有效时间对应单位
         */
        private TimeUnit validTimeUnit = TimeUnit.DAYS;
    }

    /**
     * 类描述：
     * token配置
     * @author chenf
     */
    @Data
    public static class TokenConfigInner{
        /**
         * access token的有效时间，默认单位秒
         */
        private Long accessTokenExpiration = 3600L;

        /**
         * access token的有效时间单位，默认单位秒
         */
        private TimeUnit accessTokenExpirationTimeUnit = TimeUnit.SECONDS;

        /**
         * refresh token的有效时间，默认单位秒
         */
        private Long refreshTokenExpiration = 7200L;

        /**
         * refresh token的有效时间单位，默认单位秒
         */
        private TimeUnit refreshTokenExpirationTimeUnit = TimeUnit.SECONDS;
    }
}
