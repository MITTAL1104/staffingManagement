/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.staff.dao;

import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author raghav.mittal
 */
public interface TokenDAO {

    void saveToken(int employeeId, String token, Date expiry) throws SQLException;

    boolean isTokenValid(String token) throws SQLException;

    void deleteToken(String token) throws SQLException;
}
