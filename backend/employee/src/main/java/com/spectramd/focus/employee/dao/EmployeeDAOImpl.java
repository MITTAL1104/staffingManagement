package com.spectramd.focus.employee.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.spectramd.focus.employee.entity.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private final DataSource dataSource;

    public EmployeeDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Logger logger = Logger.getLogger(EmployeeDAOImpl.class);

    private static final String GET_ALL_EMPLOYEES_QUERY = "select e.employeeId,e.name,e.email,e.dateOfJoining,e.roleId,r.roleName,e.isActive,e.isAdmin from employee_staff e join roles_staff r on e.roleId=r.roleId";

    private static final String GET_EMPLOYEE_BY_ID = "select e.employeeId,e.name,e.email,e.dateOfJoining,e.roleId,r.roleName,e.isActive,e.isAdmin from employee_staff e join roles_staff r on e.roleId=r.roleId where e.employeeId=?";

    private static final String GET_EMPLOYEE_BY_NAME = "select e.employeeId,e.name,e.email,e.dateOfJoining,e.roleId,r.roleName,e.isActive,e.isAdmin from employee_staff e join roles_staff r on e.roleId=r.roleId where e.name like ?";

    private static final String GET_EMPLOYEE_NAMES = "select name from employee_staff where cast(name AS NCHAR) like ?";

    private static final String GET_EMPLOYEE_IDS = "select employeeId from employee_staff where name like ?";

    private static final String GET_EMP_NAME_BY_EMAIL = "select name from employee_staff where email=?";

    private static final String GET_ALL_ROLES = "select roleId,roleName from roles_staff";

    private static final String GET_EMPLOYEE_BY_NAME_FOR_DELETE = "select e.employeeId,e.name,e.email,e.dateOfJoining,e.roleId,r.roleName,e.isActive,e.isAdmin from employee_staff e join roles_staff r on e.roleId=r.roleId where e.name=? and e.isActive=1";

    private static final String ADD_EMPLOYEE = "insert into employee_staff(name,email,dateOfJoining,roleId,isActive,isAdmin) values(?,?,?,(select roleId FROM roles_staff WHERE roleName = ?),?,?)";

    private static final String UPDATE_EMPLOYEE_BY_ID = "update employee set  name=?,dateOfJoining=?,roleId=?,isActive=?,isAdmin=? where employeeId=?";

    private static final String DELETE_EMPLOYEE_BY_ID = "update employee_staff set isActive = 0 where employeeId=?";

    private static final String EMP_ID_ALLOCATION_EXISTS = "select count(*) from allocation_staff where assigneeId=? and isActive=1";

    private static final String EMP_ID_IS_ACTIVE_CHECK = "select isActive from employee_staff where employeeId=?";

    private static final String EMPLOYEE_ID_EXISTS = "select count(*) from employee_staff where employeeId=?";

    private static final String GET_ALL_EMP_NAMES = "SELECT e.employeeId, e.name FROM employee_staff e JOIN roles_staff r ON e.roleId = r.roleId WHERE r.roleName IN ('Associate Development Engineer','Director of Engineering', 'Associate QA Engineer', 'Director of QA', 'Associate Data Scientist', 'Director of Analytics') AND e.isActive = 1";

    private static final String CHECK_PROJECT_OWNER = "select count(*) from project_staff where projectOwnerId=? and isActive=1";

    private static final String CHECK_DOJ_CONFLICT = "select count(*) from allocation_staff a join employee_staff e on a.assigneeId = e.employeeId where e.employeeId=? and a.isActive=1 and a.allocationStartDate<?";

    private static final String GET_EMP_ID_BY_NAME = "select employeeId from employee_staff where name=?";

    @Override
    public List<Employee> getAllEmployees(boolean onlyActive) throws SQLException {
        logger.info("Entering getAllEmployees(...) method of EmployeeDAOImpl");
        List<Employee> employees = new ArrayList<>();

        String query = GET_ALL_EMPLOYEES_QUERY;

        if (onlyActive) {
            query += " where e.isActive=1";
        }

        logger.debug("Executing SQL query: " + query);

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(query); 
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occured in getAllEmployees(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in getAllEmployees(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getAllEmployees(...) method of EmployeeDAOImpl");
        return employees;
    }

    @Override
    public Employee getEmployeeByID(int id) throws SQLException {
        logger.info("Entering getEmployeeByID(...) method of EmployeeDAOImpl");
        Employee employee = null;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_BY_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + GET_EMPLOYEE_BY_ID + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = mapRowToEmployee(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeByID(...) method of EmployeeDAOImpl");
            throw new SQLException("SQLException occured in getEmployeeByID(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeByID(...) method of EmployeeDAOImpl");
        return employee;
    }

    @Override
    public List<Employee> getEmployeeByName(String name) throws SQLException {
        logger.info("Entering getEmployeeByName(...) method of EmployeeDAOImpl");
        List<Employee> employees = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_BY_NAME)) {

            logger.debug("Executing SQL query: " + GET_EMPLOYEE_BY_NAME + " | Params: [Name=" + name + "]");
            ps.setString(1, pattern);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    employees.add(mapRowToEmployee(rs));
                }

            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeByName(...) method of EmployeeDAOImpl");
            throw new SQLException("SQLException occured in getEmployeeByName(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeByName(...) method of EmployeeDAOImpl");
        return employees;
    }

    @Override
    public List<String> getEmployeeNames(String name) throws SQLException {
        logger.info("Entering getEmployeeNames(...) method of EmployeeDAOImpl");
        List<String> names = new ArrayList<>();
        String pattern = "%" + name + "%";
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_NAMES)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_EMPLOYEE_NAMES + " | Params: [Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeNames(...) method of EmployeeDAOImpl");
            throw new SQLException("SQLException occured in getEmployeeNames(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeNames(...) method of EmployeeDAOImpl");
        return names;
    }

    @Override
    public List<Integer> getEmployeeIDsByName(String name) throws SQLException {
        logger.info("Entering getEmployeeIDsByName(...) method of EmployeeDAOImpl");
        List<Integer> Ids = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_IDS)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_EMPLOYEE_IDS + " | Params: [Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ids.add(rs.getInt("employeeId"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeIDsByName(...) method of EmployeeDAOImpl");
            throw new SQLException("SQLException occured in getEmployeeIDsByName(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeIDsByName(...) method of EmployeeDAOImpl");
        return Ids;
    }

    @Override
    public String getEmployeeNameByEmail(String email) throws SQLException {
        logger.info("Entering getEmployeeNameByEmail(...) method of EmployeeDAOImpl");
        String name = null;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMP_NAME_BY_EMAIL)) {

            ps.setString(1, email);
            logger.debug("Executing SQL query: " + GET_EMP_NAME_BY_EMAIL + " | Params: [Email=" + email + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeNameByEmail(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in getEmployeeNameByEmail(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeNameByEmail(...) method of EmployeeDAOImpl");
        return name;
    }

    @Override
    public List<Map<String, Object>> getAllRoles() throws SQLException {
        logger.info("Entering getAllRoles() method of EmployeeDAOImpl");
        List<Map<String, Object>> roles = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALL_ROLES); 
                ResultSet rs = ps.executeQuery()) {

            logger.debug("Executing SQL query: " + GET_ALL_ROLES);

            while (rs.next()) {
                Map<String, Object> role = new HashMap<>();
                role.put("roleId", rs.getInt("roleId"));
                role.put("roleName", rs.getString("roleName"));
                roles.add(role);
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getAllRoles() method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in getAllRoles() method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getAllRoles() method of EmployeeDAOImpl");
        return roles;
    }

    @Override
    public List<Map<String, Object>> getAllEmployeeNames() throws SQLException {
        logger.info("Entering getAllEmployeeNames() method of EmployeeDAOImpl");
        List<Map<String, Object>> names = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALL_EMP_NAMES); 
                ResultSet rs = ps.executeQuery()) {

            logger.debug("Executing SQL query: " + GET_ALL_EMP_NAMES);

            while (rs.next()) {
                Map<String, Object> name = new HashMap<>();
                name.put("employeeId", rs.getInt("employeeId"));
                name.put("name", rs.getString("name"));
                names.add(name);
            }

        } catch (SQLException e) {
            logger.error("SQLException occured in getAllEmployeeNames() method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in getAllEmployeeNames() method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getAllEmployeeNames() method of EmployeeDAOImpl");
        return names;
    }

    @Override
    public int getEmployeeIDByName(String name) throws SQLException {
        logger.info("Entering getEmployeeIDByName(...) method of EmployeeDAOImpl");
        int id = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMP_ID_BY_NAME)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_EMP_ID_BY_NAME + " | Params: [Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeIDByName(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in getEmployeeIDByName(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeIDByName(...) method of EmployeeDAOImpl");
        return id;
    }

    @Override
    public List<Employee> getEmployeeByNameForDelete(String name) throws SQLException {
        logger.info("Entering getEmployeeByNameForDelete(...) method of EmployeeDAOImpl");
        List<Employee> employees = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_BY_NAME_FOR_DELETE)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_EMPLOYEE_BY_NAME_FOR_DELETE + " | Params: [Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    employees.add(mapRowToEmployee(rs));
                }

            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getEmployeeByNameForDelete(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in getEmployeeByNameForDelete(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit getEmployeeByNameForDelete(...) method of EmployeeDAOImpl");
        return employees;
    }

    @Override
    public int addEmployee(Employee employee) throws SQLException {
        logger.info("Entering addEmployee(...) method of EmployeeDAOImpl");
        int rowsAffected = -1;
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(ADD_EMPLOYEE)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getDateOfJoining());
            ps.setString(4, employee.getRoleName());
            ps.setBoolean(5, employee.getIsActive());
            ps.setBoolean(6, employee.getIsAdmin());

            logger.debug("Executing SQL query: " + ADD_EMPLOYEE + " | Params:[name=" + employee.getName()
                    + ", email=" + employee.getEmail()
                    + ", dateOfJoining=" + employee.getDateOfJoining()
                    + ", roleName=" + employee.getRoleName()
                    + ", isActive=" + employee.getIsActive()
                    + ", isAdmin=" + employee.getIsAdmin() + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occured in addEmployee(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in addEmployee(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit addEmployee(...) method of EmployeeDAOImpl");
        return rowsAffected;
    }

    @Override
    public int updateEmployeeByID(Employee employee) throws SQLException {
        logger.info("Entering updateEmployeeByID(...) method of EmployeeDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(UPDATE_EMPLOYEE_BY_ID)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getDateOfJoining());
            ps.setInt(3, employee.getRoleId());
            ps.setBoolean(4, employee.getIsActive());
            ps.setBoolean(5, employee.getIsAdmin());
            ps.setInt(6, employee.getEmployeeId());

            logger.debug("Executing query: " + UPDATE_EMPLOYEE_BY_ID
                    + " | Params: [name=" + employee.getName()
                    + ", dateOfJoining=" + employee.getDateOfJoining()
                    + ", roleId=" + employee.getRoleId()
                    + ", isActive=" + employee.getIsActive()
                    + ", isAdmin=" + employee.getIsAdmin()
                    + ", employeeId=" + employee.getEmployeeId() + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occured in updateEmployeeByID(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in updateEmployeeByID(...) method of EmployeeDAOImpl", e);
        }
        logger.info("Exit updateEmployeeByID(...) method of EmployeeDAOImpl");
        return rowsAffected;
    }

    @Override
    public int deleteEmployeeByID(int id) throws SQLException {
        logger.info("Entering deleteEmployeeByID(...) method of EmployeeDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(DELETE_EMPLOYEE_BY_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + DELETE_EMPLOYEE_BY_ID + " | Params: [Employee ID=" + id + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occured in deleteEmployeeByID(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in deleteEmployeeByID(...) method of EmployeeDAOImpl", e);
        }
        logger.info("Exit deleteEmployeeByID(...) method of EmployeeDAOImpl");
        return rowsAffected;
    }

    @Override
    public boolean employeeIDAllocationExists(int id) throws SQLException {
        logger.info("Entering employeeIDAllocationExists(...) method of EmployeeDAOImpl");
        boolean exists = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(EMP_ID_ALLOCATION_EXISTS)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + EMP_ID_ALLOCATION_EXISTS + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in employeeIDAllocationExists(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in employeeIDAllocationExists(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit employeeIDAllocationExists(...) method of EmployeeDAOImpl");
        return exists;
    }

    @Override
    public boolean employeeIDIsActiveCheck(int id) throws SQLException {
        logger.info("Entering employeeIDIsActiveCheck(...) method of EmployeeDAOImpl");
        boolean idActive = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(EMP_ID_IS_ACTIVE_CHECK)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + EMP_ID_IS_ACTIVE_CHECK + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idActive = rs.getBoolean("isActive");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in employeeIDIsActiveCheck(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in employeeIDIsActiveCheck(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit employeeIDIsActiveCheck(...) method of EmployeeDAOImpl");
        return idActive;
    }

    @Override
    public boolean employeeIDExists(int id) throws SQLException {
        logger.info("Entering employeeIDExists(...) method of EmployeeDAOImpl");
        boolean exists = false;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(EMPLOYEE_ID_EXISTS)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + EMPLOYEE_ID_EXISTS + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in employeeIDExists(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in employeeIDExists(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit employeeIDExists(...) method of EmployeeDAOImpl");
        return exists;
    }

    @Override
    public boolean checkActiveProjectOwner(int id) throws SQLException {
        logger.info("Entering checkActiveProjectOwner(...) method of EmployeeDAOImpl");
        boolean isOwner = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(CHECK_PROJECT_OWNER)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + CHECK_PROJECT_OWNER + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isOwner = rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            logger.error("SQLException occured in checkActiveProjectOwner(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in checkActiveProjectOwner(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit checkActiveProjectOwner(...) method of EmployeeDAOImpl");
        return isOwner;

    }

    @Override
    public boolean checkDateOfJoiningConflict(int id, String newDateOfJoining) throws SQLException {
        logger.info("Entering checkDateOfJoiningConflict(...) method of EmployeeDAOImpl");
        boolean conflict = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(CHECK_DOJ_CONFLICT)) {

            ps.setInt(1, id);
            ps.setString(2, newDateOfJoining);
            logger.debug("Executing SQL query: " + CHECK_DOJ_CONFLICT + " | Params:[Employee ID=" + id
                    + ", New Joining Date=" + newDateOfJoining + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    conflict = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in checkDateOfJoiningConflict(...) method of EmployeeDAOImpl");
            throw new SQLException("SQL Exception occured in checkDateOfJoiningConflict(...) method of EmployeeDAOImpl", e);
        }

        logger.info("Exit checkDateOfJoiningConflict(...) method of EmployeeDAOImpl");
        return conflict;
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {

        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("employeeId"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setRoleId(rs.getInt("roleId"));
        employee.setRoleName(rs.getString("roleName"));
        employee.setDateOfJoining(rs.getString("dateOfJoining"));
        employee.setIsActive(rs.getBoolean("isActive"));
        employee.setIsAdmin(rs.getBoolean("isAdmin"));

        return employee;
    }
}
