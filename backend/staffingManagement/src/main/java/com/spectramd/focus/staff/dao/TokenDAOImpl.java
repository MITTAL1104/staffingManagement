/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raghav.mittal
 */
@Repository
public class TokenDAOImpl implements TokenDAO {

    private final DataSource dataSource;
    
    private static final Logger logger = Logger.getLogger(TokenDAOImpl.class);

    public TokenDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String SAVE_TOKEN = "insert into token_staff(employeeId,token,expiry) VALUES(?,?,?)";

    private static final String TOKEN_VALID = "select count(*) from token_staff where token=? and expiry>SYSDATETIME()";

    private static final String DELETE_TOKEN = "delete from token_staff where token=?";

    @Override
    public void saveToken(int employeeId, String token, Date expiry) throws SQLException{
        logger.info("Entering saveToken(...) method of TokenDAOImpl");
        
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(SAVE_TOKEN)) {

            ps.setInt(1, employeeId);
            ps.setString(2, token);
            ps.setTimestamp(3, new java.sql.Timestamp(expiry.getTime()));
            ps.executeUpdate();
            
            logger.info("Exit saveToken(...) method of TokenDAOImpl");
        } catch (SQLException e) {
            logger.error("SQLException occured in saveToken(...) method of TokenDAOImpl");
            throw new SQLException("SQLException occured in saveToken(...) method of TokenDAOImpl",e);
        }
    }

    @Override
    public boolean isTokenValid(String token) throws SQLException {
        boolean isValid = false;
        logger.info("Entering isTokenValid(...) method of TokenDAOImpl");
        
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(TOKEN_VALID)) {
            ps.setString(1, token);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isValid = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in isTokenValid(...) method of TokenDAOImpl");
            throw new SQLException("SQLException occured in isTokenValid(...) method of TokenDAOImpl",e);
        }

        logger.info("Exit isTokenValid(...) method of TokenDAOImpl");
        return isValid;
    }

    @Override
    public void deleteToken(String token) throws SQLException {
        logger.info("Entering deleteToken(...) method of TokenDAOImpl");
        
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(DELETE_TOKEN)) {

            ps.setString(1, token);
            ps.executeUpdate();
            logger.info("Exit deleteToken(...) method of TokenDAOImpl");
        } catch (SQLException e) {
            logger.error("SQLException occured in deleteToken(...) method of TokenDAOImpl");
            throw new SQLException("SQLException occured in deleteToken(...) method of TokenDAOImpl",e);
        }
    }

}
