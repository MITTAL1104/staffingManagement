package com.spectramd.focus.project.service;

import com.spectramd.focus.project.dao.ProjectDAO;
import com.spectramd.focus.project.entity.Project;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;

    private static final Logger logger = Logger.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public List<Project> getAllProjects(boolean onlyActive) throws SQLException {
        logger.info("Reached getAllProjects(...) method of ProjectServiceImpl");
        return projectDAO.getAllProjects(onlyActive);
    }

    @Override
    public Project getProjectByID(int id) throws SQLException {
        logger.info("Reached getProjectByID(...) method of ProjectServiceImpl");
        return projectDAO.getProjectByID(id);
    }

    @Override
    public List<Project> getProjectByName(String name) throws SQLException {
        logger.info("Reached getProjectByName(...) method of ProjectServiceImpl");
        return projectDAO.getProjectByName(name);
    }

    @Override
    public List<String> getProjectNames(String name) throws SQLException {
        logger.info("Reached getProjectNames(...) method of ProjectServiceImpl");
        return projectDAO.getProjectNames(name);
    }

    @Override
    public List<Integer> getProjectIDsByName(String name) throws SQLException {
        logger.info("Reached getProjectIDsByName(...) method of ProjectServiceImpl");
        return projectDAO.getProjectIDsByName(name);
    }

    @Override
    public List<Project> getProjectByNameForDelete(String name) throws SQLException {
        logger.info("Reached getProjectByNameForDelete(...) method of ProjectServiceImpl");
        return projectDAO.getProjectByNameForDelete(name);
    }

    @Override
    public int addProject(Project project) throws SQLException {
        logger.info("Reached addProject(...) method of ProjectServiceImpl");
        return projectDAO.addProject(project);
    }

    @Override
    public int updateProjectByID(Project project) throws SQLException {
        logger.info("Reached updateProjectByID(...) method of ProjectServiceImpl");
        return projectDAO.updateProjectByID(project);
    }

    @Override
    public int deleteProjectByID(int id) throws SQLException {
        logger.info("Reached deleteProjectByID(...) method of ProjectServiceImpl");
        return projectDAO.deleteProjectByID(id);
    }

    @Override
    public boolean projectIDAllocationExists(int id) throws SQLException {
        logger.info("Reached projectIDAllocationExists(...) method of ProjectServiceImpl");
        return projectDAO.projectIDAllocationExists(id);
    }

    @Override
    public boolean projectIDIsActiveCheck(int id) throws SQLException {
        logger.info("Reached projectIDIsActiveCheck(...) method of ProjectServiceImpl");
        return projectDAO.projectIDIsActiveCheck(id);
    }

    @Override
    public boolean projectIDExists(int id) throws SQLException {
        logger.info("Reached projectIDExists(...) method of ProjectServiceImpl");
        return projectDAO.projectIDExists(id);
    }

    @Override
    public boolean checkProjectStartDateConflict(int id, String newStartDate) throws SQLException {
        logger.info("Reached checkProjectStartDateConflict(...) method of ProjectServiceImpl");
        return projectDAO.checkProjectStartDateConflict(id, newStartDate);
    }

    @Override
    public boolean checkProjectEndDateConflict(int id, String newEndDate) throws SQLException {
        logger.info("Reached checkProjectEndDateConflict(...) method of ProjectServiceImpl");
        return projectDAO.checkProjectEndDateConflict(id, newEndDate);
    }
}
