package com.goudong.authentication.client.util;

import com.goudong.authentication.client.core.UserSimple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 * jwt解析
 * @author cfl
 */
@Slf4j
public class GoudongJwtUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 解析token
     * @param secretKey 应用密钥
     * @param token 令牌
     * @return  UserSimple 用户简单对象
     */
    public static UserSimple parseToken(String secretKey, String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return JsonUtil.toObject(body.getSubject(), UserSimple.class);
        } catch (ExpiredJwtException e) {
            log.error("解析token失败：{}", e.getMessage());
            throw new IllegalArgumentException("解析令牌失败，令牌无效");
        }
    }
}
