package com.map.toolbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtils {

    private final String secretKey = "!DnJJ_([&'2}eZnq6`-&Yp6'@}<G0L=zqcd1f5W^!i&x_q(nXpMXBy)3LK<`K&U";
    private Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis()+ 2* 60* 10000))
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 10000))
                .withIssuer("ProjectApp")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public DecodedJWT decodeJwt(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

}
