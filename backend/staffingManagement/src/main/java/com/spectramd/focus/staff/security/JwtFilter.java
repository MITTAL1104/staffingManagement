/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
package com.spectramd.focus.staff.security;

import com.spectramd.focus.staff.dao.TokenDAO;
import io.jsonwebtoken.Claims;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final TokenDAO tokenDAO;

    public JwtFilter(JwtUtil jwtUtil, TokenDAO tokenDAO) {
        this.jwtUtil = jwtUtil;
        this.tokenDAO = tokenDAO;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        if (null!=request.getCookies()) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (null!=token) {
            try {
                Claims claims = jwtUtil.extractClaims(token);

                if (!tokenDAO.isTokenValid(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token is invalid or logged out");
                    return;
                }

                String email = claims.getSubject();
                Integer employeeId = claims.get("employeeId", Integer.class);

                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
