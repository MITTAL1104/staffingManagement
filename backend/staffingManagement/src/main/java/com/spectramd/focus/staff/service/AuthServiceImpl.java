/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.service;

import com.spectramd.focus.staff.dao.TokenDAO;
import com.spectramd.focus.staff.dao.UserCredentialsDAO;
import com.spectramd.focus.staff.entity.UserCredentials;
import com.spectramd.focus.staff.security.JwtUtil;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author raghav.mittal
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserCredentialsDAO userDAO;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final TokenDAO tokenDAO;

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserCredentialsDAO userDAO, TokenDAO tokenDAO, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenDAO = tokenDAO;
    }

    @Override
    public void login(String email, String password, HttpServletResponse response) throws SQLException {
        logger.info("Reached login(...) method of AuthServiceImpl");

        UserCredentials user = userDAO.findByEmail(email);

        if (null != user && passwordEncoder.matches(password, user.getPassword())) {

            String token = jwtUtil.generateToken(user.getEmail(), user.getEmployeeId(), user.getIsAdmin());

            Date expiryDate = jwtUtil.extractClaims(token).getExpiration();
            tokenDAO.saveToken(user.getEmployeeId(), token, expiryDate);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Override
    public void logout(String token) throws SQLException {
        logger.info("Reached logout(...) method of AuthServiceImpl");
        tokenDAO.deleteToken(token);
    }

    @Override
    public void register(UserCredentials user) throws SQLException {
        logger.info("Reached register(...) method of AuthServiceImpl");
        if (!userDAO.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email not found. Registration denied!");
        }

        if (userDAO.emailCredsExists(user.getEmail())) {
            throw new IllegalArgumentException("User already exists!");
        }

        int employeeId = userDAO.getEmployeeIdByEmail(user.getEmail());
        boolean isAdmin = userDAO.getIsAdminByEmail(user.getEmail());

        user.setEmployeeId(employeeId);
        user.setIsAdmin(isAdmin);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        logger.debug("Saving user details and credentials");
        userDAO.save(user);
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        logger.info("Reached emailExists(...) method of AuthServiceImpl");
        return userDAO.emailExists(email);
    }

    @Override
    public boolean emailCredsExists(String email) throws SQLException {
        logger.info("Reached emailCredsExists(...) method of AuthServiceImpl");
        return userDAO.emailCredsExists(email);
    }

    @Override
    public void registerWithDetails(Map<String, String> userData) throws SQLException {
        logger.info("Reached registerWithDetails(...) method of AuthServiceImpl");

        String email = userData.get("email");
        String password = passwordEncoder.encode(userData.get("password"));
        String name = userData.get("name");
        String role = userData.get("role");
        String dateOfJoining = userData.get("dateOfJoining");

        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        logger.debug("Creating a new employee");
        userDAO.insertIntoEmployeeTable(name, email, dateOfJoining, role);

        int employeeId = userDAO.getEmployeeIdByEmail(email);

        UserCredentials creds = new UserCredentials();
        creds.setEmail(email);
        creds.setPassword(password);
        creds.setEmployeeId(employeeId);
        creds.setIsAdmin(Boolean.FALSE);

        logger.debug("Saving user details and credentials");
        userDAO.save(creds);
    }

    @Override
    public int getEmployeeIdByEmail(String email) throws SQLException {
        logger.info("Reached getEmployeeIdByEmail(...) method of AuthServiceImpl");
        return userDAO.getEmployeeIdByEmail(email);
    }

    @Override
    public boolean getIsAdminByEmail(String email) throws SQLException {
        logger.info("Reached getIsAdminByEmail(...) method of AuthServiceImpl");
        return userDAO.getIsAdminByEmail(email);
    }

    @Override
    public void updatePassword(String email, String oldPassword, String newPassword) throws SQLException {
        logger.info("Reached updatePassword(...) method of AuthServiceImpl");
        UserCredentials user = userDAO.findByEmail(email);

        if (null == user) {
            throw new RuntimeException("User not found!");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect!");
        }

        String encryptedNewPassword = passwordEncoder.encode(newPassword);
        logger.debug("Updating password in credentials table");
        userDAO.updatePassword(email, encryptedNewPassword);
    }
}
