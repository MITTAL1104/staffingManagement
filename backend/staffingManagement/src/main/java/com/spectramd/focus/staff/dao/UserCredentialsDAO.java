/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.staff.dao;

import com.spectramd.focus.staff.entity.UserCredentials;
import java.sql.SQLException;

/**
 *
 * @author raghav.mittal
 */
public interface UserCredentialsDAO {

    UserCredentials findByEmail(String email) throws SQLException;

    void save(UserCredentials user) throws SQLException;

    public boolean emailExists(String email) throws SQLException;

    public boolean emailCredsExists(String email) throws SQLException;

    public int getEmployeeIdByEmail(String email) throws SQLException;

    public boolean getIsAdminByEmail(String email) throws SQLException;

    void updatePassword(String email, String newPassword) throws SQLException;

    void insertIntoEmployeeTable(String name, String email, String dateOfJoining, String role) throws SQLException;
}
