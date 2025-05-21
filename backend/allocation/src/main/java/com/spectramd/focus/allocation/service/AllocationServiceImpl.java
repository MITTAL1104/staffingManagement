/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.allocation.service;

import com.spectramd.focus.allocation.dao.AllocationDAO;
import com.spectramd.focus.allocation.entity.Allocation;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author raghav.mittal
 */
@Service
public class AllocationServiceImpl implements AllocationService {

    private final AllocationDAO allocationDAO;

    private static final Logger logger = Logger.getLogger(AllocationServiceImpl.class);

    public AllocationServiceImpl(AllocationDAO allocationDAO) {
        this.allocationDAO = allocationDAO;
    }

    @Override
    public List<Allocation> getAllAllocations(boolean onlyActive) throws SQLException {
        logger.info("Reached getAllAllocations(...) method of AllocationServiceImpl");
        return allocationDAO.getAllAllocations(onlyActive);
    }

    @Override
    public Allocation getByAllocationID(int id) throws SQLException {
        logger.info("Reached getByAllocationID(...) method of AllocationServiceImpl");
        return allocationDAO.getByAllocationID(id);
    }

    @Override
    public List<Allocation> getAllAllocationByEmployeeName(String name) throws SQLException {
        logger.info("Reached getAllAllocationByEmployeeName(...) method of AllocationServiceImpl");
        return allocationDAO.getAllAllocationByEmployeeName(name);
    }

    @Override
    public List<Allocation> getAllocationByProjectName(String name) throws SQLException {
        logger.info("Reached getAllocationByProjectName(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocationByProjectName(name);
    }

    @Override
    public List<Allocation> getByEmployeeID(int id) throws SQLException {
        logger.info("Reached getByEmployeeID(...) method of AllocationServiceImpl");
        return allocationDAO.getByEmployeeID(id);
    }

    @Override
    public List<Allocation> getAssigneeIDActiveAllocation(int id) throws SQLException {
        logger.info("Reached getAssigneeIDActiveAllocation(...) method of AllocationServiceImpl");
        return allocationDAO.getAssigneeIDActiveAllocation(id);
    }

    @Override
    public List<String> getAllocatedEmployeeNames(String name) throws SQLException {
        logger.info("Reached getAllocatedEmployeeNames(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocatedEmployeeNames(name);
    }

    @Override
    public List<String> getAllocatedProjectNames(String name) throws SQLException {
        logger.info("Reached getAllocatedProjectNames(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocatedProjectNames(name);
    }

    @Override
    public List<Integer> getAllocatedIDs(String name) throws SQLException {
        logger.info("Reached getAllocatedIDs(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocatedIDs(name);
    }

    @Override
    public List<Integer> getAllocatedEmployeeIDs(String name) throws SQLException {
        logger.info("Reached getAllocatedEmployeeIDs(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocatedEmployeeIDs(name);
    }

    @Override
    public List<Allocation> getAllocationForDeleteByEmployeeName(String name) throws SQLException {
        logger.info("Reached getAllocationForDeleteByEmployeeName(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocationForDeleteByEmployeeName(name);
    }

    @Override
    public List<Allocation> getAllocationForDeleteByProjectName(String name) throws SQLException {
        logger.info("Reached getAllocationForDeleteByProjectName(...) method of AllocationServiceImpl");
        return allocationDAO.getAllocationForDeleteByProjectName(name);
    }

    @Override
    public int getEmployeeIDByName(String name) throws SQLException {
        logger.info("Reached getEmployeeIDByName(...) method of AllocationServiceImpl");
        return allocationDAO.getEmployeeIDByName(name);
    }

    @Override
    public int getProjectIDByName(String name) throws SQLException {
        logger.info("Reached getProjectIDByName(...) method of AllocationServiceImpl");
        return allocationDAO.getProjectIDByName(name);
    }

    @Override
    public int addAllocation(Allocation allocation) throws SQLException {
        logger.info("Reached addAllocation(...) method of AllocationServiceImpl");
        return allocationDAO.addAllocation(allocation);
    }

    @Override
    public int updateAllocation(Allocation allocation) throws SQLException {
        logger.info("Reached updateAllocation(...) method of AllocationServiceImpl");
        return allocationDAO.updateAllocation(allocation);
    }

    @Override
    public int deleteByAllocationID(int id) throws SQLException {
        logger.info("Reached deleteByAllocationID(...) method of AllocationServiceImpl");
        return allocationDAO.deleteByAllocationID(id);
    }

    @Override
    public boolean allocationIDExists(int id) throws SQLException {
        logger.info("Reached allocationIDExists(...) method of AllocationServiceImpl");
        return allocationDAO.allocationIDExists(id);
    }

    @Override
    public boolean employeeJoiningDateCheck(String allocationStartDate, int assigneeId) throws SQLException {
        logger.info("Reached employeeJoiningDateCheck(...) method of AllocationServiceImpl");
        return allocationDAO.employeeJoiningDateCheck(allocationStartDate, assigneeId);
    }

    @Override
    public boolean projectDatesCheck(String allocationStartDate, String allocationEndDate, int projectId) throws SQLException {
        logger.info("Reached projectDatesCheck(...) method of AllocationServiceImpl");
        return allocationDAO.projectDatesCheck(allocationStartDate, allocationEndDate, projectId);
    }

    @Override
    public boolean datesOverlapCheckForActiveAllocation(int assigneeId, String newStartDateStr, String newEndDateStr) throws SQLException {

        logger.info("Entering datesOverlapCheckForActiveAllocation(...) method of AllocationServiceImpl");
        LocalDate newStartDate = LocalDate.parse(newStartDateStr);
        LocalDate newEndDate = LocalDate.parse(newEndDateStr);
        boolean flag = false;

        List<Allocation> activeAllocations = allocationDAO.getAssigneeIDActiveAllocation(assigneeId);

        for (Allocation existing : activeAllocations) {
            LocalDate existingStart = LocalDate.parse(existing.getAllocationStartDate());
            LocalDate existingEnd = LocalDate.parse(existing.getAllocationEndDate());

            if ((newStartDate.isBefore(existingEnd) && newEndDate.isAfter(existingStart))) {
                flag = true;
            }
        }

        logger.info("Exit datesOverlapCheckForActiveAllocation(...) method of AllocationServiceImpl");
        return flag;
    }

    @Override
    public boolean isAllocationExistsForSameProject(int assigneeId, int projectId) throws SQLException {
        logger.info("Reached isAllocationExistsForSameProject(...) method of AllocationServiceImpl");
        return allocationDAO.isAllocationExistsForSameProject(assigneeId, projectId);
    }

    @Override
    public boolean employeeIsActiveCheck(int assigneeId) throws SQLException {
        logger.info("Reached employeeIsActiveCheck(...) method of AllocationServiceImpl");
        return allocationDAO.employeeIsActiveCheck(assigneeId);
    }

    @Override
    public boolean projectIsActiveCheck(int projectId) throws SQLException {
        logger.info("Reached projectIsActiveCheck(...) method of AllocationServiceImpl");
        return allocationDAO.projectIsActiveCheck(projectId);
    }
}
