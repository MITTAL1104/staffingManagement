/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author raghav.mittal
 */
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "raghavmittal";
    
    private static final Logger logger = Logger.getLogger(JwtUtil.class);

    public String generateToken(String email, int employeeId, boolean isAdmin) {
        logger.info("Reached generateToken(...) method of JwtUtil");
        return Jwts.builder()
                .setSubject(email)
                .claim("employeeId", employeeId)
                .claim("isAdmin", isAdmin)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) {
        logger.info("Reached extractClaims(...) method of JwtUtil");
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        logger.info("Reached validateToken(...) method of JwtUtil");
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;  
        }
    }
    
    public String extractTokenFromRequest(HttpServletRequest request) {
        logger.info("Reached extractTokenFromRequest(...) method of JwtUtil");
        if (null!=request.getCookies()) {
            for (Cookie cookie : request.getCookies()) {
                if (("jwt").equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
