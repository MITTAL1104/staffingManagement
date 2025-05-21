package com.spectramd.focus.employee.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spectramd.focus.employee.dao.EmployeeDAO;
import com.spectramd.focus.employee.entity.Employee;
import java.sql.SQLException;
import java.util.Map;
import org.apache.log4j.Logger;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;

    private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> getAllEmployees(boolean onlyActive) throws SQLException {
        logger.info("Reached getAllEmployees(...) method of EmployeeServiceImpl");
        return employeeDAO.getAllEmployees(onlyActive);
    }

    @Override
    public Employee getEmployeeByID(int id) throws SQLException {
        logger.info("Reached getEmployeeByID(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeByID(id);
    }

    @Override
    public List<Employee> getEmployeeByName(String name) throws SQLException {
        logger.info("Reached getEmployeeByName(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeByName(name);
    }

    @Override
    public List<String> getEmployeeNames(String name) throws SQLException {
        logger.info("Reached getEmployeeNames(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeNames(name);
    }

    @Override
    public List<Integer> getEmployeeIDsByName(String name) throws SQLException {
        logger.info("Reached getEmployeeIDsByName(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeIDsByName(name);
    }

    @Override
    public String getEmployeeNameByEmail(String email) throws SQLException {
        logger.info("Reached getEmployeeNameByEmail(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeNameByEmail(email);
    }

    @Override
    public List<Map<String, Object>> getAllRoles() throws SQLException {
        logger.info("Reached getAllRoles() method of EmployeeServiceImpl");
        return employeeDAO.getAllRoles();
    }

    @Override
    public int getEmployeeIDByName(String name) throws SQLException {
        logger.info("Reached getEmployeeIDByName(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeIDByName(name);
    }

    @Override
    public List<Map<String, Object>> getAllEmployeeNames() throws SQLException {
        logger.info("Reached getAllEmployeeNames() method of EmployeeServiceImpl");
        return employeeDAO.getAllEmployeeNames();
    }

    @Override
    public List<Employee> getEmployeeByNameForDelete(String name) throws SQLException {
        logger.info("Reached getEmployeeByNameForDelete(...) method of EmployeeServiceImpl");
        return employeeDAO.getEmployeeByNameForDelete(name);
    }

    @Override
    public int addEmployee(Employee employee) throws SQLException {
        logger.info("Reached addEmployee(...) method of EmployeeServiceImpl");
        return employeeDAO.addEmployee(employee);
    }

    @Override
    public int updateEmployeeByID(Employee employee) throws SQLException {
        logger.info("Reached updateEmployeeByID(...) method of EmployeeServiceImpl");
        return employeeDAO.updateEmployeeByID(employee);
    }

    @Override
    public int deleteEmployeeByID(int id) throws SQLException {
        logger.info("Reached deleteEmployeeByID(...) method of EmployeeServiceImpl");
        return employeeDAO.deleteEmployeeByID(id);
    }

    @Override
    public boolean employeeIDAllocationExists(int id) throws SQLException {
        logger.info("Reached employeeIDAllocationExists(...) method of EmployeeServiceImpl");
        return employeeDAO.employeeIDAllocationExists(id);
    }

    @Override
    public boolean employeeIDIsActiveCheck(int id) throws SQLException {
        logger.info("Reached employeeIDIsActiveCheck(...) method of EmployeeServiceImpl");
        return employeeDAO.employeeIDIsActiveCheck(id);
    }

    @Override
    public boolean employeeIDExists(int id) throws SQLException {
        logger.info("Reached employeeIDExists(...) method of EmployeeServiceImpl");
        return employeeDAO.employeeIDExists(id);
    }

    @Override
    public boolean checkActiveProjectOwner(int id) throws SQLException {
        logger.info("Reached checkActiveProjectOwner(...) method of EmployeeServiceImpl");
        return employeeDAO.checkActiveProjectOwner(id);
    }

    @Override
    public boolean checkDateOfJoiningConflict(int id, String newDateOfJoining) throws SQLException {
        logger.info("Reached checkDateOfJoiningConflict(...) method of EmployeeServiceImpl");
        return employeeDAO.checkDateOfJoiningConflict(id, newDateOfJoining);
    }
}
