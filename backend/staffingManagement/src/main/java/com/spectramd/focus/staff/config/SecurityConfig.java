package com.spectramd.focus.staff.config;

import com.spectramd.focus.staff.dao.TokenDAO;
import com.spectramd.focus.staff.security.JwtFilter;
import com.spectramd.focus.staff.security.JwtUtil;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final TokenDAO tokenDAO;

    public SecurityConfig(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    public static final Logger logger = Logger.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:3000")); // FRONTEND URL
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                .antMatchers("/staff/login", "/staff/register", "/staff/registerWithDetails", "/staff/details").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtil, tokenDAO), BasicAuthenticationFilter.class)
                .logout(logout -> logout
                .logoutUrl("/staff/logout")
                .logoutSuccessHandler((request, response, authentication) -> {

                    String token = null;
                    if (null != request.getCookies()) {
                        for (Cookie cookie : request.getCookies()) {
                            if ("jwt".equals(cookie.getName())) {
                                token = cookie.getValue();
                                break;
                            }
                        }
                    }

                    if (token != null && !token.isEmpty()) {
                        try {
                            tokenDAO.deleteToken(token);
                            logger.info("Token deleted from database");
                        } catch (SQLException ex) {
                            logger.error("Error deleting token from database");
                        }
                    }

                    ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(0)
                            .build();

                    response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Logout Successful");
                }));

        return http.build();
    }

}
