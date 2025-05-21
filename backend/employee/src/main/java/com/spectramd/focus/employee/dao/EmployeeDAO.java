package com.spectramd.focus.employee.dao;

import java.util.List;

import com.spectramd.focus.employee.entity.Employee;
import java.sql.SQLException;
import java.util.Map;

public interface EmployeeDAO {

    List<Employee> getAllEmployees(boolean onlyActive) throws SQLException;

    Employee getEmployeeByID(int id) throws SQLException;

    List<Employee> getEmployeeByName(String name) throws SQLException;

    List<String> getEmployeeNames(String name) throws SQLException;

    List<Integer> getEmployeeIDsByName(String name) throws SQLException;

    String getEmployeeNameByEmail(String email) throws SQLException;

    List<Map<String, Object>> getAllRoles() throws SQLException;

    List<Map<String, Object>> getAllEmployeeNames() throws SQLException;

    int getEmployeeIDByName(String name) throws SQLException;

    List<Employee> getEmployeeByNameForDelete(String name) throws SQLException;

    int addEmployee(Employee employee) throws SQLException;

    int updateEmployeeByID(Employee employee) throws SQLException;

    int deleteEmployeeByID(int id) throws SQLException;

    boolean employeeIDAllocationExists(int id) throws SQLException;

    boolean employeeIDIsActiveCheck(int id) throws SQLException;

    boolean employeeIDExists(int id) throws SQLException;

    boolean checkActiveProjectOwner(int id) throws SQLException;

    boolean checkDateOfJoiningConflict(int id, String newDateOfJoining) throws SQLException;
}
