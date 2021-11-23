package org.lxzx.boot.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.lxzx.boot.bean.User;

import java.util.Date;

public class TokenUtil {
    public static final int EXPIRE_TIME = 10 * 60 * 60 * 1000;
    private static final String TOKEN_SCRETE="HUANG";

    public static String sign(User user) {
        String token = null;
        try {
            Date exprisesAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userName", user.getUserName())
                    .withExpiresAt(exprisesAt)
                    .sign(Algorithm.HMAC256(TOKEN_SCRETE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SCRETE)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }

}
