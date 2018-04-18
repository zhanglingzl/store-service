package com.rxr.store.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
@Slf4j
public class JWTHelper {

    private static final String  ISSUER = "rxr_user";


    public static String createToken(String loginName, String password){
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTCreator.Builder builder = JWT.create()
                    .withClaim("loginName", loginName)
                    .withIssuer(ISSUER)
                    .withExpiresAt(DateUtils.addDays(new Date(), 1));
            return builder.sign(algorithm);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String token,String loginName, String password)  {
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("loginName", loginName)
                    .withIssuer(ISSUER).build();
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            log.warn("token验证出错:" + ex.getMessage());

        }
        return false;
    }

    public static String getLoginName(String token)  {
        DecodedJWT jwt =  JWT.decode(token);
        return jwt.getClaim("loginName").asString();
    }
}