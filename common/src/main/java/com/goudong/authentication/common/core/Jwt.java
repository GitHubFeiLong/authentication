package com.goudong.authentication.common.core;

import com.goudong.authentication.common.constant.CommonConst;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.boot.web.core.ClientException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * jwt创建和解析
 * @author cfl
 */
@Slf4j
public class Jwt {

    //~fields
    //==================================================================================================================
    /**
     * access token的有效时间
     */
    private Long accessTokenExpiration = CommonConst.ACCESS_TOKEN_EXPIRATION;

    /**
     * access token的有效时间单位
     */
    private TimeUnit accessTokenExpirationTimeUnit = CommonConst.ACCESS_TOKEN_EXPIRATION_TIME_UNIT;

    /**
     * refresh token的有效时间
     */
    private Long refreshTokenExpiration = CommonConst.REFRESH_TOKEN_EXPIRATION;

    /**
     * refresh token的有效时间单位
     */
    private TimeUnit refreshTokenExpirationTimeUnit = CommonConst.REFRESH_TOKEN_EXPIRATION_TIME_UNIT;

    /**
     * 密钥
     */
    private final String secretKey;


    //~methods
    //==================================================================================================================
    /**
     * 构造方法，创建jwt实例
     * @param accessTokenExpiration             access token的有效时间
     * @param accessTokenExpirationTimeUnit     access token的有效时间单位
     * @param refreshTokenExpiration            refresh token的有效时间
     * @param refreshTokenExpirationTimeUnit    refresh token的有效时间单位
     * @param secretKey                         app密钥
     */
    public Jwt(long accessTokenExpiration, TimeUnit accessTokenExpirationTimeUnit, long refreshTokenExpiration, TimeUnit refreshTokenExpirationTimeUnit, String secretKey) {
        this.accessTokenExpiration = accessTokenExpiration;
        this.accessTokenExpirationTimeUnit = accessTokenExpirationTimeUnit;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.refreshTokenExpirationTimeUnit = refreshTokenExpirationTimeUnit;
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建token
     * @param userSimple
     * @return
     */
    public Token generateToken(UserSimple userSimple) {
        // 获取失效时间
        Date now = new Date();
        long accessTokenExpirationTimeUnitMillis = this.accessTokenExpirationTimeUnit.toMillis(this.accessTokenExpiration);
        long refreshTokenExpirationTimeUnitMillis = this.refreshTokenExpirationTimeUnit.toMillis(this.refreshTokenExpiration);
        Date accessExpiration = new Date(now.getTime() + accessTokenExpirationTimeUnitMillis);
        Date refreshExpiration = new Date(now.getTime() + refreshTokenExpirationTimeUnitMillis);

        String json = JsonUtil.toJsonString(userSimple);
        String accessToken = Jwts.builder()
                .setSubject(json)
                .setIssuedAt(now)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(json)
                .setIssuedAt(now)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setAccessExpires(accessExpiration);
        token.setRefreshExpires(refreshExpiration);

        return token;
    }

    /**
     * 解析token
     * @param token 令牌
     * @return  UserSimple 用户简单对象
     */
    public UserSimple parseToken(String token) {
        return Jwt.parseToken(this.secretKey, token);
    }


    /**
     * 解析token
     * @param secretKey 应用密钥
     * @param token 令牌
     * @return  UserSimple 用户简单对象
     */
    public static UserSimple parseToken(String secretKey, String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .parseClaimsJws(token)
                    .getBody();
            return JsonUtil.toObject(body.getSubject(), UserSimple.class);
        } catch (ExpiredJwtException e) {
            log.error("解析token失败：{}", e.getMessage());
            throw ClientException.clientByUnauthorized();
        }
    }
}
