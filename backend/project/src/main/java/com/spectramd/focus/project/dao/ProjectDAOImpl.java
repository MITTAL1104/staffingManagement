package com.spectramd.focus.project.dao;

import com.spectramd.focus.project.entity.Project;
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

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    @Autowired
    private final DataSource dataSource;

    public ProjectDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Logger logger = Logger.getLogger(ProjectDAOImpl.class);

    private static final String GET_ALL_PROJECTS_QUERY = "select p.projectId,p.projectName,p.description,e.name as projectOwnerName ,p.projectOwnerId,p.startDate,p.endDate,p.isActive from project_staff p join employee_staff e on p.projectOwnerId = e.employeeId";

    private static final String GET_PROJECT_BY_ID = "select p.projectId,p.projectName,p.description,e.name as projectOwnerName ,p.projectOwnerId,p.startDate,p.endDate,p.isActive from project_staff p join employee_staff e on p.projectOwnerId = e.employeeId where projectId=?";

    private static final String GET_PROJECT_BY_NAME = "select p.projectId,p.projectName,p.description,e.name as projectOwnerName,p.projectOwnerId,p.startDate,p.endDate,p.isActive from project_staff p join employee_staff e on p.projectOwnerId = e.employeeId where projectName like ?";

    private static final String GET_PROJECT_NAMES = "select projectName from project_staff where cast(projectName AS NCHAR) like ?";

    private static final String GET_PROJECT_IDS = "select projectId from project_staff where projectName like ?";

    private static final String ADD_PROJECT = "insert into project_staff(projectName,description,projectOwnerId,startDate,endDate,isActive) values(?,?,(select employeeId from employee_staff where name=?),?,?,?)";

    private static final String GET_PROJECT_BY_NAME_FOR_DELETE = "select p.projectId,p.projectName,p.description,e.name as projectOwnerName,p.projectOwnerId,p.startDate,p.endDate,p.isActive from project_staff p join employee_staff e on p.projectOwnerId = e.employeeId where projectName=? and p.isActive=1";

    private static final String UPDATE_PROJECT_BY_ID = "update project_staff set projectName=?,description=?,projectOwnerId=(select employeeId from employee_staff where name=?),startDate=?,endDate=?,isActive=? where projectId=?";

    private static final String DELETE_PROJECT_BY_ID = "update project_staff set isActive=0 where projectId=?";

    private static final String PROJ_ID_ALLOCATION_EXISTS = "select count(*) from allocation_staff where projectId=? and isActive=1";

    private static final String PROJ_ID_IS_ACTIVE_CHECK = "select isActive from project_staff where projectId=?";

    private static final String PROJECT_ID_EXISTS = "select count(*) from project_staff where projectId=?";

    private static final String PROJ_START_DATE_CONFLICT = "select count(*) from allocation_staff a join project_staff p on a.projectId = p.projectId where p.projectId=? and a.isActive=1 and a.allocationStartDate<?";

    private static final String PROJ_END_DATE_CONFLICT = "select count(*) from allocation_staff a join project_staff p on a.projectId = p.projectId where p.projectId=? and a.isActive=1 and a.allocationEndDate>?";

    @Override
    public List<Project> getAllProjects(boolean onlyActive) throws SQLException {
        logger.info("Entering getAllProjects(...) method of ProjectDAOImpl");
        List<Project> projects = new ArrayList<>();

        String query = GET_ALL_PROJECTS_QUERY;

        if (onlyActive) {
            query += " where p.isActive=1";
        }

        logger.debug("Executing SQL query: " + query);

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(query); 
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }

        } catch (SQLException e) {
            logger.error("SQLException occured in getAllProjects(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in getAllProjects(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit getAllProjects(...) method of ProjectDAOImpl");
        return projects;
    }

    @Override
    public Project getProjectByID(int id) throws SQLException {
        logger.info("Entering getProjectByID(...) method of ProjectDAOImpl");
        Project project = null;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_PROJECT_BY_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + GET_PROJECT_BY_ID + " | Params: [Project ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    project = mapRowToProject(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("SQLException occured in getProjectByID(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in getProjectByID(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit getProjectByID(...) method of ProjectDAOImpl");
        return project;
    }

    @Override
    public List<Project> getProjectByName(String name) throws SQLException {
        logger.info("Entering getProjectByName(...) method of ProjectDAOImpl");
        List<Project> projects = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_PROJECT_BY_NAME)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_PROJECT_BY_NAME + " | Params: [ Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapRowToProject(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("SQLException occured in getProjectByName(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in getProjectByName(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit getProjectByName(...) method of ProjectDAOImpl");
        return projects;
    }

    @Override
    public List<String> getProjectNames(String name) throws SQLException {
        logger.info("Entering getProjectNames(...) method of ProjectDAOImpl");
        List<String> names = new ArrayList<>();
        String pattern = "%" + name + "%";
        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_PROJECT_NAMES)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_PROJECT_NAMES + " | Params: [Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("projectName"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getProjectNames(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in getProjectNames(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit getProjectNames(...) method of ProjectDAOImpl");
        return names;
    }

    @Override
    public List<Integer> getProjectIDsByName(String name) throws SQLException {
        logger.info("Entering getProjectIDsByName(...) method of ProjectDAOImpl");
        List<Integer> Ids = new ArrayList<>();
        String pattern = "%" + name + "%";

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_PROJECT_IDS)) {

            ps.setString(1, pattern);
            logger.debug("Executing SQL query: " + GET_PROJECT_IDS + " | Params: [Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ids.add(rs.getInt("projectId"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getProjectIDsByName(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in getProjectIDsByName(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit getProjectIDsByName(...) method of ProjectDAOImpl");
        return Ids;
    }

    @Override
    public List<Project> getProjectByNameForDelete(String name) throws SQLException {
        logger.info("Entering getProjectByNameForDelete(...) method of ProjectDAOImpl");
        List<Project> projects = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(GET_PROJECT_BY_NAME_FOR_DELETE)) {

            ps.setString(1, name);
            logger.debug("Executing SQL query: " + GET_PROJECT_BY_NAME_FOR_DELETE + " | Params: [Project Name=" + name + "]");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    projects.add(mapRowToProject(rs));
                }

            }
        } catch (SQLException e) {
            logger.error("SQLException occured in getProjectByNameForDelete(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in getProjectByNameForDelete(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit getProjectByNameForDelete(...) method of ProjectDAOImpl");
        return projects;
    }

    @Override
    public int addProject(Project project) throws SQLException {
        logger.info("Entering addProject(...) method of ProjectDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(ADD_PROJECT)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getProjectOwnerName());
            ps.setString(4, project.getStartDate());
            ps.setString(5, project.getEndDate());
            ps.setBoolean(6, project.getIsActive());

            logger.debug("Executing SQL query: " + ADD_PROJECT + " | Params:[projectName=" + project.getProjectName()
                    + ", description=" + project.getDescription()
                    + ", projectOwnerName=" + project.getProjectOwnerName()
                    + ", startDate=" + project.getStartDate()
                    + ", endDate=" + project.getEndDate()
                    + ", isActive=" + project.getIsActive() + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occured in addProject(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in addProject(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit addProject(...) method of ProjectDAOImpl");
        return rowsAffected;
    }

    @Override
    public int updateProjectByID(Project project) throws SQLException {
        logger.info("Entering updateProjectByID(...) method of ProjectDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(UPDATE_PROJECT_BY_ID)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getProjectOwnerName());
            ps.setString(4, project.getStartDate());
            ps.setString(5, project.getEndDate());
            ps.setBoolean(6, project.getIsActive());
            ps.setInt(7, project.getProjectId());

            logger.debug("Executing SQL query: " + UPDATE_PROJECT_BY_ID + " | Params:[projectName=" + project.getProjectName()
                    + ", description=" + project.getDescription()
                    + ", projectOwnerName=" + project.getProjectOwnerName()
                    + ", startDate=" + project.getStartDate()
                    + ", endDate=" + project.getEndDate()
                    + ", isActive=" + project.getIsActive() + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occured in updateProjectByID(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in updateProjectByID(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit updateProjectByID(...) method of ProjectDAOImpl");
        return rowsAffected;
    }

    @Override
    public int deleteProjectByID(int id) throws SQLException {
        logger.info("Entering deleteProjectByID(...) method of ProjectDAOImpl");
        int rowsAffected = -1;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(DELETE_PROJECT_BY_ID)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + DELETE_PROJECT_BY_ID + " | Params: [Project ID=" + id + "]");

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException occured in deleteProjectByID(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in deleteProjectByID(...) method of ProjectDAOImpl", e);
        }
        logger.info("Exit deleteProjectByID(...) method of ProjectDAOImpl");
        return rowsAffected;
    }

    @Override
    public boolean projectIDAllocationExists(int id) throws SQLException {
        logger.info("Entering projectIDAllocationExists(...) method of ProjectDAOImpl");
        boolean exists = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(PROJ_ID_ALLOCATION_EXISTS)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + PROJ_ID_ALLOCATION_EXISTS + " | Params: [Project ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in projectIDAllocationExists(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in projectIDAllocationExists(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit projectIDAllocationExists(...) method of ProjectDAOImpl");
        return exists;
    }

    @Override
    public boolean projectIDIsActiveCheck(int id) throws SQLException {
        logger.info("Entering projectIDIsActiveCheck(...) method of ProjectDAOImpl");
        boolean idActive = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(PROJ_ID_IS_ACTIVE_CHECK)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + PROJ_ID_IS_ACTIVE_CHECK + " | Params: [Project ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idActive = rs.getBoolean("isActive");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in projectIDIsActiveCheck(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in projectIDIsActiveCheck(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit projectIDIsActiveCheck(...) method of ProjectDAOImpl");
        return idActive;
    }

    @Override
    public boolean projectIDExists(int id) throws SQLException {
        logger.info("Entering projectIDExists(...) method of ProjectDAOImpl");
        boolean exists = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(PROJECT_ID_EXISTS)) {

            ps.setInt(1, id);
            logger.debug("Executing SQL query: " + PROJECT_ID_EXISTS + " | Params: [Project ID=" + id + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in projectIDExists(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in projectIDExists(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit projectIDExists(...) method of EmployeeDAOImpl");
        return exists;
    }

    @Override
    public boolean checkProjectStartDateConflict(int id, String newStartDate) throws SQLException {
        logger.info("Entering checkProjectStartDateConflict(...) method of ProjectDAOImpl");
        boolean conflict = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(PROJ_START_DATE_CONFLICT)) {

            ps.setInt(1, id);
            ps.setString(2, newStartDate);
            logger.debug("Executing SQL query: " + PROJ_START_DATE_CONFLICT + " | Params:[Project ID=" + id
                    + ", New Start Date=" + newStartDate + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    conflict = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in checkProjectStartDateConflict(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in checkProjectStartDateConflict(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit checkProjectStartDateConflict(...) method of ProjectDAOImpl");
        return conflict;
    }

    @Override
    public boolean checkProjectEndDateConflict(int id, String newEndDate) throws SQLException {
        logger.info("Entering checkProjectEndDateConflict(...) method of ProjectDAOImpl");
        boolean conflict = false;

        try (
                Connection connection = dataSource.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(PROJ_END_DATE_CONFLICT)) {

            ps.setInt(1, id);
            ps.setString(2, newEndDate);
            logger.debug("Executing SQL query: " + PROJ_END_DATE_CONFLICT + " | Params:[Project ID=" + id
                    + ", New End Date=" + newEndDate + "]");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    conflict = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occured in checkProjectEndDateConflict(...) method of ProjectDAOImpl");
            throw new SQLException("SQLException occured in checkProjectEndDateConflict(...) method of ProjectDAOImpl", e);
        }

        logger.info("Exit checkProjectEndDateConflict(...) method of ProjectDAOImpl");
        return conflict;
    }

    private Project mapRowToProject(ResultSet rs) throws SQLException {

        Project project = new Project();
        project.setProjectId(rs.getInt("projectId"));
        project.setProjectName(rs.getString("projectName"));
        project.setDescription(rs.getString("description"));
        project.setProjectOwnerId(rs.getInt("projectOwnerId"));
        project.setProjectOwnerName(rs.getString("projectOwnerName"));
        project.setStartDate(rs.getString("startDate"));
        project.setEndDate(rs.getString("endDate"));
        project.setIsActive(rs.getBoolean("isActive"));

        return project;
    }

}
