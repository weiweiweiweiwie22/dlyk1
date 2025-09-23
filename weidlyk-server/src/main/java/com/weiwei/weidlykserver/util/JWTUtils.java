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
     * 新的生成方法，直接接收userId和userJSON
     */
    public static String createJWT(Integer userId, String userJSON) {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return JWT.create()
                .withHeader(header)
                .withClaim("userId", userId) // 单独存放userId
                .withClaim("user", userJSON) // 存放完整的user对象JSON
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证JWT。如果验证失败，此方法会抛出业务异常。
     */
    public static DecodedJWT verifyJWT(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            return jwtVerifier.verify(jwt);
        } catch (TokenExpiredException e) {
            throw new BusinessException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}