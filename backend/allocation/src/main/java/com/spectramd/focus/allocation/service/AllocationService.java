/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.allocation.service;

import com.spectramd.focus.allocation.entity.Allocation;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author raghav.mittal
 */
public interface AllocationService {

    List<Allocation> getAllAllocations(boolean onlyActive) throws SQLException;

    Allocation getByAllocationID(int id) throws SQLException;

    List<Allocation> getAllAllocationByEmployeeName(String name) throws SQLException;

    List<Allocation> getAllocationByProjectName(String name) throws SQLException;

    List<Allocation> getByEmployeeID(int id) throws SQLException;

    List<Allocation> getAssigneeIDActiveAllocation(int id) throws SQLException;

    List<String> getAllocatedEmployeeNames(String name) throws SQLException;

    List<String> getAllocatedProjectNames(String name) throws SQLException;

    List<Integer> getAllocatedIDs(String name) throws SQLException;

    List<Integer> getAllocatedEmployeeIDs(String name) throws SQLException;

    List<Allocation> getAllocationForDeleteByEmployeeName(String name) throws SQLException;

    List<Allocation> getAllocationForDeleteByProjectName(String name) throws SQLException;

    int getEmployeeIDByName(String name) throws SQLException;

    int getProjectIDByName(String name) throws SQLException;

    int addAllocation(Allocation allocation) throws SQLException;

    int updateAllocation(Allocation allocation) throws SQLException;

    int deleteByAllocationID(int id) throws SQLException;

    boolean allocationIDExists(int id) throws SQLException;

    public boolean employeeJoiningDateCheck(String allocationStartDate, int assigneeId) throws SQLException;

    public boolean projectDatesCheck(String allocationStartDate, String allocationEndDate, int projectId) throws SQLException;

    public boolean datesOverlapCheckForActiveAllocation(int assigneeId, String newStartDateStr, String newEndDateStr) throws SQLException;

    public boolean isAllocationExistsForSameProject(int assigneeId, int projectId) throws SQLException;

    public boolean employeeIsActiveCheck(int assigneeId) throws SQLException;

    public boolean projectIsActiveCheck(int projectId) throws SQLException;
}
