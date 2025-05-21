package com.spectramd.focus.project.dao;

import com.spectramd.focus.project.entity.Project;
import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {

    List<Project> getAllProjects(boolean onlyActive) throws SQLException;

    Project getProjectByID(int id) throws SQLException;

    List<Project> getProjectByName(String name) throws SQLException;

    List<String> getProjectNames(String name) throws SQLException;

    List<Integer> getProjectIDsByName(String name) throws SQLException;
    
    List<Project> getProjectByNameForDelete(String name) throws SQLException;

    int addProject(Project project) throws SQLException;

    int updateProjectByID(Project project) throws SQLException;

    int deleteProjectByID(int id) throws SQLException;

    boolean projectIDAllocationExists(int id) throws SQLException;

    boolean projectIDIsActiveCheck(int id) throws SQLException;
    
    boolean projectIDExists(int id) throws SQLException;
    
    boolean checkProjectStartDateConflict(int id, String newStartDate) throws SQLException;

    boolean checkProjectEndDateConflict(int id, String newEndDate) throws SQLException;
}
