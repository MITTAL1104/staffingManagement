/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.dao;

import com.spectramd.focus.staff.entity.UserCredentials;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raghav.mittal
 */
@Repository
public class UserCredentialsDAOImpl implements UserCredentialsDAO {

    @Autowired
    private final DataSource dataSource;
    
    private static final Logger logger = Logger.getLogger(UserCredentialsDAOImpl.class);

    public UserCredentialsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String FIND_BY_EMAIL = "select userId, email, password, employeeId, isAdmin from credentials_staff where email=?";

    private static final String ADD_USER = "insert into credentials_staff(email,password,employeeId,isAdmin) values(?,?,?,?)";

    private static final String EMAIL_EXISTS = "select count(*) from employee_staff where email=?";

    private static final String EMAIL_CREDS_EXISTS = "select count(*) from credentials_staff where email=?";

    private static final String FETCH_EMP_ID = "select employeeId from employee_staff where email=?";

    private static final String FETCH_IS_ADMIN = "select isAdmin from employee_staff where email=?";

    private static final String UPDATE_PASSWORD = "update credentials_staff set password=? where email=?";

    private static final String INSERT_EMPLOYEE = "insert into employee_staff(name,email,dateOfJoining,roleId,isActive,isAdmin) values(?,?,?,(select roleId FROM roles_staff WHERE roleName = ?),1,0)";

    @Override
    public UserCredentials findByEmail(String email) throws SQLException {
        logger.info("Entering findByEmail(...) method of UserCredentialsDAOImpl");
        UserCredentials user = null;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapRowToUserCredentials(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in findByEmail(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in findByEmail(...) method of UserCredentialsDAOImpl",e);
        }
        
        logger.info("Exit findByEmail(...) method of UserCredentialsDAOImpl");
        return user;
    }

    @Override
    public void save(UserCredentials user) throws SQLException{
        logger.info("Entering save(...) method of UserCredentialsDAOImpl");
        
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(ADD_USER)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getEmployeeId());
            ps.setBoolean(4, user.getIsAdmin());
            ps.executeUpdate();
            
            logger.info("Exit save(...) method of UserCredentialsDAOImpl");
        } catch (SQLException e) {
            logger.error("SQLException occured in save(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in save(...) method of UserCredentialsDAOImpl",e);
        }
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        logger.info("Entering emailExists(...) method of UserCredentialsDAOImpl");
        boolean exists = false;
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(EMAIL_EXISTS)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in emailExists(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in emailExists(...) method of UserCredentialsDAOImpl",e);
        }
        logger.info("Exit emailExists(...) method of UserCredentialsDAOImpl");
        return exists;
    }

    @Override
    public boolean emailCredsExists(String email) throws SQLException {
        logger.info("Entering emailCredsExists(...) method of UserCredentialsDAOImpl");
        boolean exists = false;
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(EMAIL_CREDS_EXISTS)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in emailCredsExists(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in emailCredsExists(...) method of UserCredentialsDAOImpl",e);
        }
        
        logger.info("Exit emailCredsExists(...) method of UserCredentialsDAOImpl");
        return exists;
    }

    @Override
    public int getEmployeeIdByEmail(String email) throws SQLException {
        logger.info("Entering getEmployeeIdByEmail(...) method of UserCredentialsDAOImpl");
        
        int employeeId = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(FETCH_EMP_ID)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employeeId = rs.getInt("employeeId");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeIdByEmail(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in getEmployeeIdByEmail(...) method of UserCredentialsDAOImpl",e);
        }

        logger.info("Exit getEmployeeIdByEmail(...) method of UserCredentialsDAOImpl");
        return employeeId;
    }

    @Override
    public boolean getIsAdminByEmail(String email) throws SQLException {
        logger.info("Entering getIsAdminByEmail(...) method of UserCredentialsDAOImpl");
        
        boolean isAdmin = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(FETCH_IS_ADMIN)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isAdmin = rs.getBoolean("isAdmin");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getIsAdminByEmail(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in getIsAdminByEmail(...) method of UserCredentialsDAOImpl",e);
        }

        logger.info("Exit getIsAdminByEmail(...) method of UserCredentialsDAOImpl");
        return isAdmin;
    }

    @Override
    public void updatePassword(String email, String password) throws SQLException {
        logger.info("Entering updatePassword(...) method of UserCredentialsDAOImpl");
        
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(UPDATE_PASSWORD)) {

            ps.setString(1, password);
            ps.setString(2, email);
            ps.executeUpdate();
            logger.info("Exit updatePassword(...) method of UserCredentialsDAOImpl");
        } catch (SQLException e) {
            logger.error("SQLException occured in updatePassword(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in updatePassword(...) method of UserCredentialsDAOImpl",e);
        }
    }

    @Override
    public void insertIntoEmployeeTable(String name, String email, String dateOfJoining, String role) throws SQLException {
        logger.info("Entering insertIntoEmployeeTable(...) method of UserCredentialsDAOImpl");
        
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(INSERT_EMPLOYEE)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, dateOfJoining);
            ps.setString(4, role);
            ps.executeUpdate();
            logger.info("Exit insertIntoEmployeeTable(...) method of UserCredentialsDAOImpl");
        } catch (SQLException e) {
            logger.error("SQLException occured in insertIntoEmployeeTable(...) method of UserCredentialsDAOImpl");
            throw new SQLException("SQLException occured in insertIntoEmployeeTable(...) method of UserCredentialsDAOImpl",e);
        }
    }

    private UserCredentials mapRowToUserCredentials(ResultSet rs) throws SQLException {
        
        UserCredentials user = new UserCredentials();
        user.setUserId(rs.getInt("userId"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setEmployeeId(rs.getInt("employeeId"));
        user.setIsAdmin(rs.getBoolean("isAdmin"));
        
        return user;
    }

}
