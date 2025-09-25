package com.weiwei.weidlykserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.result.ResultCodeEnum;

import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    public static final String SECRET = "dY8300olWQ3345;1d<3w48";

    /**
     * 【修改】生成JWT的方法，增加了 rememberMe 参数
     * @param userId 用户ID
     * @param userJSON 用户信息的JSON字符串
     * @param rememberMe 是否记住我
     * @return JWT 字符串
     */
    public static String createJWT(Integer userId, String userJSON, boolean rememberMe) {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return JWT.create()
                .withHeader(header)
                .withClaim("userId", userId) // 单独存放userId
                .withClaim("user", userJSON) // 存放完整的user对象JSON
                .withClaim("rememberMe", rememberMe) // 【新增】存放 '是否记住我' 的状态
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证JWT。如果验证失败，此方法会抛出业务异常。
     * (此方法无需改动)
     */
    public static DecodedJWT verifyJWT(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            return jwtVerifier.verify(jwt);
        } catch (TokenExpiredException e) {
            // 注意：这个异常可能不会被触发，因为我们主要依赖Redis的过期。
            // 但保留它是好的实践，以防JWT本身的过期时间比Redis短。
            throw new BusinessException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}