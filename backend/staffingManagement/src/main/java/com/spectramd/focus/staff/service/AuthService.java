/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.staff.service;

import com.spectramd.focus.staff.entity.UserCredentials;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author raghav.mittal
 */
public interface AuthService {

    void login(String email, String password, HttpServletResponse response) throws SQLException;

    void logout(String token) throws SQLException;

    void register(UserCredentials user) throws SQLException;

    public boolean emailExists(String email) throws SQLException;

    public boolean emailCredsExists(String email) throws SQLException;

    public int getEmployeeIdByEmail(String email) throws SQLException;

    public boolean getIsAdminByEmail(String email) throws SQLException;

    void updatePassword(String email, String oldPassword, String newPassword) throws SQLException;

    void registerWithDetails(Map<String, String> userData) throws SQLException;
}
