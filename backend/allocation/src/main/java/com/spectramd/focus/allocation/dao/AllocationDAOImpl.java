/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.allocation.dao;

import com.spectramd.focus.allocation.entity.Allocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raghav.mittal
 */
@Repository
public class AllocationDAOImpl implements AllocationDAO {

    @Autowired
    private final DataSource dataSource;

    private static final Logger logger = Logger.getLogger(AllocationDAOImpl.class);

    public AllocationDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String GET_ALL_ALLOCATIONS_QUERY = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive from allocation_staff a join employee_staff e on a.assigneeId = e.employeeId join project_staff p on a.projectId = p.projectId";

    private static final String GET_ALLOCATION_BY_ID = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive from allocation_staff a join employee_staff e on a.assigneeId = e.employeeId join project_staff p on a.projectId = p.projectId  where allocationId=?";

    private static final String GET_ALLOCATION_BY_EMP_NAME = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive FROM allocation_staff a JOIN employee_staff e ON a.assigneeId=e.employeeId  JOIN project_staff p ON a.projectId = p.projectId WHERE e.name like ?";

    private static final String GET_ALLOCATION_BY_PROJ_NAME = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive FROM allocation_staff a JOIN employee_staff e ON a.assigneeId = e.employeeId JOIN project_staff p ON a.projectId = p.projectId WHERE p.projectName like ?";

    private static final String GET_ALLOCATION_BY_EMPLOYEE_ID = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive from allocation_staff a JOIN employee_staff e ON a.assigneeId=e.employeeId join project_staff p ON a.projectId=p.projectId WHERE a.assigneeId=?";

    private static final String GET_EMP_ID_ACTIVE_ALLOCATIONS = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive from allocation_staff a JOIN employee_staff e ON a.assigneeId=e.employeeId join project_staff p ON a.projectId=p.projectId WHERE a.assigneeId=? AND a.isActive=1";

    private static final String GET_ALLOCATED_EMPLOYEE_NAMES = "select e.name from employee_staff e JOIN allocation_staff a ON e.employeeId = a.assigneeId where e.name like ?";

    private static final String GET_ALLOCATED_PROJECT_NAMES = "select p.projectName FROM employee_staff e JOIN allocation_staff  a ON e.employeeId = a.assigneeId  JOIN project_staff p ON a.projectId = p.projectId WHERE e.name like ?";

    private static final String GET_ALLOCATED_IDS = "select a.allocationId FROM employee_staff e JOIN allocation_staff  a ON e.employeeId = a.assigneeId WHERE e.name like ?";

    private static final String GET_ALLOCATED_EMPLOYEE_IDS = "select e.employeeId from employee_staff e join allocation_staff a on e.employeeId = a.assigneeId where e.name like ?";

    private static final String GET_ALLOC_FOR_DELETE_BY_EMP_NAME = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive FROM allocation_staff a JOIN employee_staff e ON a.assigneeId=e.employeeId  JOIN project_staff p ON a.projectId = p.projectId WHERE e.name=? AND a.isActive=1";

    private static final String GET_ALLOC_FOR_DELETE_BY_PROJ_NAME = "select a.allocationId,a.assigneeId,e.name as assigneeName,a.projectId,p.projectName as projectName,a.allocationStartDate,a.allocationEndDate,a.allocatorName,a.percentageAllocation,a.isActive FROM allocation_staff a JOIN employee_staff e ON a.assigneeId=e.employeeId  JOIN project_staff p ON a.projectId = p.projectId WHERE p.projectName=?  AND a.isActive=1";

    private static final String GET_ASSIGNEE_ID = "select employeeId from employee_staff where name=?";

    private static final String GET_PROJECT_ID = "select projectId from project_staff where projectName=?";

    private static final String ADD_ALLOCATION = "insert into allocation_staff(assigneeId,projectId,allocationStartDate,allocationEndDate,allocatorName,percentageAllocation,isActive) values(?,?,?,?,?,?,?)";

    private static final String UPDATE_ALLOCATION = "update allocation_staff set allocationStartDate=?,allocationEndDate=?,isActive=? where allocationId=?";

    private static final String DELETE_ALLOCATION_BY_ID = "update allocation_staff set isActive=0 where allocationId=?";

    private static final String ALLOCATION_ID_EXISTS = "select count(*) from allocation_staff where allocationId=?";

    private static final String ALLOCATION_EMP_JOINING_DATE_CHECK = "select case when ? >=e.dateOfJoining then cast(1 as BIT) ELSE cast(0 as BIT) end as isValid FROM employee_staff e where e.employeeId=?";

    private static final String ALLOCATION_PROJ_DATES_CHECK = "select case when ?>=p.startDate and ?<=p.endDate then cast(1 as BIT) ELSE cast(0 as BIT) end as validProjAlloc FROM project_staff p where p.projectId=?";

    private static final String SAME_PROJECT_ALLOCATION_CHECK = "select count(*) from allocation_staff where assigneeId=? and projectId=? and isActive=1";

    private static final String EMP_ACTIVE_CHECK = "select isActive from employee_staff where employeeId=?";

    private static final String PROJ_ACTIVE_CHECK = "select isActive from project_staff where projectId=?";

    @Override
    public List<Allocation> getAllAllocations(boolean onlyActive) throws SQLException {
        logger.info("Entering getAllAllocations(...) method of AllocatideonDAOImpl");
        List<Allocation> allocations = new ArrayList<>();
        String query = GET_ALL_ALLOCATIONS_QUERY;

        if (onlyActive) {
            query += " where a.isActive=1";
        }

        logger.debug("Executing SQL query: " + query);

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(query); 
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                allocations.add(mapRowToAllocation(rs));
            }

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllAllocations(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllAllocations(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllAllocations(...) method of AllocationDAOImpl");
        return allocations;
    }

    @Override
    public Allocation getByAllocationID(int id) throws SQLException {
        logger.info("Entering getByAllocationID(...) method of AllocationDAOImpl");
        Allocation allocation = null;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATION_BY_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + GET_ALLOCATION_BY_ID + " | Params: [Allocation ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    allocation = mapRowToAllocation(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("SQLException occurred in getByAllocationID(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getByAllocationID(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getByAllocationID(...) method of AllocationDAOImpl");
        return allocation;
    }

    @Override
    public List<Allocation> getAllAllocationByEmployeeName(String name) throws SQLException {
        logger.info("Entering getAllAllocationByEmployeeName(...) method of AllocationDAOImpl");
        List<Allocation> allocations = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATION_BY_EMP_NAME)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_ALLOCATION_BY_EMP_NAME + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocations.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllAllocationByEmployeeName(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllAllocationByEmployeeName(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllAllocationByEmployeeName(...) method of AllocationDAOImpl");
        return allocations;
    }

    @Override
    public List<Allocation> getAllocationByProjectName(String name) throws SQLException {
        logger.info("Entering getAllocationByProjectName(...) method of AllocationDAOImpl");
        List<Allocation> allocations = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATION_BY_PROJ_NAME)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_ALLOCATION_BY_PROJ_NAME + " | Params: [ Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocations.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationByProjectName(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocationByProjectName(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocationByProjectName(...) method of AllocationDAOImpl");
        return allocations;
    }

    @Override
    public List<Allocation> getByEmployeeID(int id) throws SQLException {
        logger.info("Entering getByEmployeeID(...) method of AllocationDAOImpl");
        List<Allocation> allocations = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATION_BY_EMPLOYEE_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + GET_ALLOCATION_BY_EMPLOYEE_ID + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocations.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getByEmployeeID(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getByEmployeeID(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getByEmployeeID(...) method of AllocationDAOImpl");
        return allocations.isEmpty() ? null : allocations;
    }

    @Override
    public List<Allocation> getAssigneeIDActiveAllocation(int id) throws SQLException {
        logger.info("Entering getAssigneeIDActiveAllocation(...) method of AllocationDAOImpl");
        List<Allocation> allocations = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_EMP_ID_ACTIVE_ALLOCATIONS)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + GET_EMP_ID_ACTIVE_ALLOCATIONS + " | Params: [Employee ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocations.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAssigneeIDActiveAllocation(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAssigneeIDActiveAllocation(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAssigneeIDActiveAllocation(...) method of AllocationDAOImpl");
        return allocations;
    }

    @Override
    public List<String> getAllocatedEmployeeNames(String name) throws SQLException {
        logger.info("Entering getAllocatedEmployeeNames(...) method of AllocationDAOImpl");
        List<String> names = new ArrayList<>();
        String pattern = "%" + name + "%";
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATED_EMPLOYEE_NAMES)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_ALLOCATED_EMPLOYEE_NAMES + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedEmployeeNames(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocatedEmployeeNames(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocatedEmployeeNames(...) method of AllocationDAOImpl");
        return names;
    }

    @Override
    public List<String> getAllocatedProjectNames(String name) throws SQLException {
        logger.info("Entering getAllocatedProjectNames(...) method of AllocationDAOImpl");
        List<String> names = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATED_PROJECT_NAMES)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_ALLOCATED_PROJECT_NAMES + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("projectName"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedProjectNames(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocatedProjectNames(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocatedProjectNames(...) method of AllocationDAOImpl");
        return names;
    }

    @Override
    public List<Integer> getAllocatedIDs(String name) throws SQLException {
        logger.info("Entering getAllocatedIDs(...) method of AllocationDAOImpl");
        List<Integer> ids = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATED_IDS)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_ALLOCATED_IDS + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("allocationId"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedIDs(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocatedIDs(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocatedIDs(...) method of AllocationDAOImpl");
        return ids;
    }

    @Override
    public List<Integer> getAllocatedEmployeeIDs(String name) throws SQLException {
        logger.info("Entering getAllocatedEmployeeIDs(...) method of AllocationDAOImpl");
        List<Integer> ids = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOCATED_EMPLOYEE_IDS)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_ALLOCATED_EMPLOYEE_IDS + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("employeeId"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedEmployeeIDs(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocatedEmployeeIDs(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocatedEmployeeIDs(...) method of AllocationDAOImpl");
        return ids;
    }

    @Override
    public List<Allocation> getAllocationForDeleteByEmployeeName(String name) throws SQLException {
        logger.info("Entering getAllocationForDeleteByEmployeeName(...) method of AllocationDAOImpl");
        List<Allocation> allocations = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOC_FOR_DELETE_BY_EMP_NAME)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_ALLOC_FOR_DELETE_BY_EMP_NAME + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocations.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationForDeleteByEmployeeName(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocationForDeleteByEmployeeName(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocationForDeleteByEmployeeName(...) method of AllocationDAOImpl");
        return allocations;
    }

    @Override
    public List<Allocation> getAllocationForDeleteByProjectName(String name) throws SQLException {
        logger.info("Entering getAllocationForDeleteByProjectName(...) method of AllocationDAOImpl");
        List<Allocation> allocations = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ALLOC_FOR_DELETE_BY_PROJ_NAME)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_ALLOC_FOR_DELETE_BY_PROJ_NAME + " | Params: [ Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocations.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationForDeleteByProjectName(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getAllocationForDeleteByProjectName(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getAllocationForDeleteByProjectName(...) method of AllocationDAOImpl");
        return allocations;
    }

    @Override
    public int getEmployeeIDByName(String name) throws SQLException {
        logger.info("Entering getEmployeeIDByName(...) method of AllocationDAOImpl");
        int empId = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_ASSIGNEE_ID)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_ASSIGNEE_ID + " | Params: [ Employee Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empId = rs.getInt("employeeId");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeIDByName(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getEmployeeIDByName(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getEmployeeIDByName(...) method of AllocationDAOImpl");
        return empId;
    }

    @Override
    public int getProjectIDByName(String name) throws SQLException {
        logger.info("Entering getProjectIDByName(...) method of AllocationDAOImpl");
        int projId = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_PROJECT_ID)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_PROJECT_ID + " | Params: [ Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    projId = rs.getInt("projectId");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectIDByName(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in getProjectIDByName(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit getProjectIDByName(...) method of AllocationDAOImpl");
        return projId;
    }

    @Override
    public int addAllocation(Allocation allocation) throws SQLException {
        logger.info("Entering addAllocation(...) method of AllocationDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(ADD_ALLOCATION)) {

            ps.setInt(1, allocation.getAssigneeId());
            ps.setInt(2, allocation.getProjectId());
            ps.setString(3, allocation.getAllocationStartDate());
            ps.setString(4, allocation.getAllocationEndDate());
            ps.setString(5, allocation.getAllocatorName());
            ps.setInt(6, allocation.getPercentageAllocation());
            ps.setBoolean(7, allocation.getIsActive());

            logger.debug("Executing SQL query: " + ADD_ALLOCATION + " | Params: [assigneeId=" + allocation.getAssigneeId()
                    + ", projectId=" + allocation.getProjectId()
                    + ", allocationStartDate=" + allocation.getAllocationStartDate()
                    + ", allocationEndDate=" + allocation.getAllocationEndDate()
                    + ", allocatorName=" + allocation.getAllocatorName()
                    + ", percentageAllocation=" + allocation.getPercentageAllocation()
                    + ", isActive=" + allocation.getIsActive() + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occurred in addAllocation(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in addAllocation(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit addAllocation(...) method of AllocationDAOImpl");
        return rowsAffected;
    }

    @Override
    public int updateAllocation(Allocation allocation) throws SQLException {
        logger.info("Entering updateAllocation(...) method of AllocationDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(UPDATE_ALLOCATION)) {

            ps.setString(1, allocation.getAllocationStartDate());
            ps.setString(2, allocation.getAllocationEndDate());
            ps.setBoolean(3, allocation.getIsActive());
            ps.setInt(4, allocation.getAllocationId());

            logger.debug("Executing SQL query: " + UPDATE_ALLOCATION + " | Params: [assigneeId=" + allocation.getAssigneeId()
                    + ", projectId=" + allocation.getProjectId()
                    + ", allocationStartDate=" + allocation.getAllocationStartDate()
                    + ", allocationEndDate=" + allocation.getAllocationEndDate()
                    + ", allocatorName=" + allocation.getAllocatorName()
                    + ", percentageAllocation=" + allocation.getPercentageAllocation()
                    + ", isActive=" + allocation.getIsActive() + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occurred in updateAllocation(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in updateAllocation(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit updateAllocation(...) method of AllocationDAOImpl");
        return rowsAffected;
    }

    @Override
    public int deleteByAllocationID(int id) throws SQLException {
        logger.info("Entering deleteByAllocationID(...) method of AllocationDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(DELETE_ALLOCATION_BY_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + DELETE_ALLOCATION_BY_ID + " | Params: [Allocation ID=" + id + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occurred in deleteByAllocationID(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in deleteByAllocationID(...) method of AllocationDAOImpl", e);
        }
        logger.info("Exit deleteByAllocationID(...) method of AllocationDAOImpl");
        return rowsAffected;
    }

    @Override
    public boolean allocationIDExists(int id) throws SQLException {
        logger.info("Entering allocationIDExists(...) method of AllocationDAOImpl");
        boolean exists = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(ALLOCATION_ID_EXISTS)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + ALLOCATION_ID_EXISTS + " | Params: [Allocation ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in allocationIDExists(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in allocationIDExists(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit allocationIDExists(...) method of AllocationDAOImpl");
        return exists;
    }

    @Override
    public boolean employeeJoiningDateCheck(String allocationStartDate, int assigneeId) throws SQLException {
        logger.info("Entering employeeJoiningDateCheck(...) method of AllocationDAOImpl");
        boolean isValid = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(ALLOCATION_EMP_JOINING_DATE_CHECK)) {
            ps.setString(1, allocationStartDate);
            ps.setInt(2, assigneeId);
            logger.debug("Executing SQL query: " + ALLOCATION_EMP_JOINING_DATE_CHECK + " | Params:[Employee ID=" + assigneeId
                    + ", Allocation Start Date=" + allocationStartDate + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isValid = rs.getInt("isValid") == 1;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in employeeJoiningDateCheck(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in employeeJoiningDateCheck(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit employeeJoiningDateCheck(...) method of AllocationDAOImpl");
        return isValid;
    }

    @Override
    public boolean projectDatesCheck(String allocationStartDate, String allocationEndDate, int projectId) throws SQLException {
        logger.info("Entering projectDatesCheck(...) method of AllocationDAOImpl");
        boolean isValid = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(ALLOCATION_PROJ_DATES_CHECK)) {
            ps.setString(1, allocationStartDate);
            ps.setString(2, allocationEndDate);
            ps.setInt(3, projectId);
            logger.debug("Executing SQL query: " + ALLOCATION_PROJ_DATES_CHECK + " | Params:[Project ID=" + projectId
                    + ", Allocation Start Date=" + allocationStartDate
                    + ", Allocation End Date=" + allocationEndDate + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isValid = rs.getInt("validProjAlloc") == 1;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in projectDatesCheck(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in projectDatesCheck(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit projectDatesCheck(...) method of AllocationDAOImpl");
        return isValid;
    }

    @Override
    public boolean isAllocationExistsForSameProject(int assigneeId, int projectId) throws SQLException {
        logger.info("Entering isAllocationExistsForSameProject(...) method of AllocationDAOImpl");
        boolean exists = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(SAME_PROJECT_ALLOCATION_CHECK)) {

            ps.setInt(1, assigneeId);
            ps.setInt(2, projectId);
            logger.debug("Executing SQL query: " + SAME_PROJECT_ALLOCATION_CHECK + " | Params: [Employee ID=" + assigneeId
                    + ", Project ID=" + projectId + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in isAllocationExistsForSameProject(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in isAllocationExistsForSameProject(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit isAllocationExistsForSameProject(...) method of AllocationDAOImpl");
        return exists;
    }

    @Override
    public boolean employeeIsActiveCheck(int assigneeId) throws SQLException {
        logger.info("Entering employeeIsActiveCheck(...) method of AllocationDAOImpl");
        boolean active = false;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(EMP_ACTIVE_CHECK)) {

            ps.setInt(1, assigneeId);
            logger.debug("Executing SQL query: " + EMP_ACTIVE_CHECK + " | Params:[Employee ID=" + assigneeId + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    active = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in employeeIsActiveCheck(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in employeeIsActiveCheck(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit employeeIsActiveCheck(...) method of AllocationDAOImpl");
        return active;
    }

    @Override
    public boolean projectIsActiveCheck(int projectId) throws SQLException {
        logger.info("Entering projectIsActiveCheck(...) method of AllocationDAOImpl");
        boolean active = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(PROJ_ACTIVE_CHECK)) {

            ps.setInt(1, projectId);
            logger.debug("Executing SQL query: " + PROJ_ACTIVE_CHECK + " | Params:[Project ID=" + projectId + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    active = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred in projectIsActiveCheck(...) method of AllocationDAOImpl");
            throw new SQLException("SQLException occurred in projectIsActiveCheck(...) method of AllocationDAOImpl", e);
        }

        logger.info("Exit projectIsActiveCheck(...) method of AllocationDAOImpl");
        return active;
    }

    private Allocation mapRowToAllocation(ResultSet rs) throws SQLException {

        Allocation allocation = new Allocation();
        allocation.setAllocationId(rs.getInt("allocationId"));
        allocation.setAssigneeId(rs.getInt("assigneeId"));
        allocation.setAssigneeName(rs.getString("assigneeName"));
        allocation.setProjectId(rs.getInt("projectId"));
        allocation.setProjectName(rs.getString("projectName"));
        allocation.setAllocationStartDate(rs.getString("allocationStartDate"));
        allocation.setAllocationEndDate(rs.getString("allocationEndDate"));
        allocation.setAllocatorName(rs.getString("allocatorName"));
        allocation.setPercentageAllocation(rs.getInt("percentageAllocation"));
        allocation.setIsActive(rs.getBoolean("isActive"));

        return allocation;
    }
}
