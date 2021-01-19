package com.kay.utils;

import com.alibaba.fastjson.JSONArray;
import com.kay.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtUtils {

    public static final String TOKEN_HEADER = "token";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SUBJECT = "mayikt";

    private static final long EXPIRITION = 1000 * 24 * 60 * 60 * 7;

    private static final String APPSECRET_KEY = "kay_secret";

    private static final String ROLE_CLAIMS = "roles";

    public static String generateJsonWebToken(UserEntity user) {
        String token = Jwts
                .builder()
                .setSubject(SUBJECT)
                .claim(ROLE_CLAIMS, user.getAuthorities())
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }

    /**
     * 生成token
     *
     * @param username
     * @param role
     * @return
     */
    public static String createToken(String username, String role) {

        Map<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);

        String token = Jwts
                .builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }

    public static Claims checkJWT(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    /**
     * 获取用户角色
     *
     * @param token
     * @return
     */
    public static List<SimpleGrantedAuthority> getUserRole(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        List roles = (List) claims.get(ROLE_CLAIMS);
        String json = JSONArray.toJSONString(roles);
        List<SimpleGrantedAuthority>
                grantedAuthorityList =
                JSONArray.parseArray(json, SimpleGrantedAuthority.class);

        return grantedAuthorityList;
    }

    /**
     * 是否过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }

}
