package com.message.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.util.List;

public class JWTCreator {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorities";

    // ========================
    // CREATE TOKEN
    // ========================
    public static String create(String prefix, String key, JWTObject jwtObject) {

        Key signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(key)
        );

        String token = Jwts.builder()
                .setSubject(jwtObject.getSubject())
                .setIssuedAt(jwtObject.getIssuedAt())
                .setExpiration(jwtObject.getExpiration())
                // 🔥 AGORA SALVA APENAS STRING
                .claim(ROLES_AUTHORITIES, normalizeRoles(jwtObject.getRoles()))
                .signWith(signingKey)
                .compact();

        return prefix + " " + token;
    }

    // ========================
    // PARSE TOKEN
    // ========================
    @SuppressWarnings("unchecked")
    public static JWTObject parse(String token, String prefix, String key)
            throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException {

        Key signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(key)
        );

        token = token.replace(prefix, "").trim();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        JWTObject object = new JWTObject();
        object.setSubject(claims.getSubject());
        object.setIssuedAt(claims.getIssuedAt());
        object.setExpiration(claims.getExpiration());
        object.setRoles((List<String>) claims.get(ROLES_AUTHORITIES));

        return object;
    }

    // ========================
    // ROLE NORMALIZATION
    // ========================
    private static List<String> normalizeRoles(List<String> roles) {
        return roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .toList();
    }
}
