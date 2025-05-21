package com.spectramd.focus.project.controller;

import com.spectramd.focus.project.entity.Project;
import com.spectramd.focus.project.service.ProjectExcelExportService;
import com.spectramd.focus.project.service.ProjectService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final ProjectExcelExportService projectExcelExportService;

    private static final Logger logger = Logger.getLogger(ProjectController.class);

    public ProjectController(ProjectService projectService, ProjectExcelExportService projectExcelExportService) {
        this.projectService = projectService;
        this.projectExcelExportService = projectExcelExportService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProjects(@RequestParam boolean onlyActive) {
        try {
            if (onlyActive) {
                logger.info("Fetching all active projects");
            } else {
                logger.info("Fetching all projects");
            }

            List<Project> projects = projectService.getAllProjects(onlyActive);
            return ResponseEntity.ok(projects);

        } catch (SQLException e) {
            logger.error("SQlException occured in getAllProjects(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured while fetching projects");
        }
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getProjectByID(@PathVariable int id) {
        logger.info("Fetching details of project with id: " + id);
        try {
            return ResponseEntity.ok(projectService.getProjectByID(id));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectByID(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching project by ID");
        }
    }

    @GetMapping("getAllByName/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        logger.info("Fetching details of all projects with matching name: " + name);
        try {
            return ResponseEntity.ok(projectService.getProjectByName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectByName(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching projects by name");
        }
    }

    @GetMapping("/getNames/{name}")
    public ResponseEntity<?> getProjectNames(@PathVariable String name) {
        logger.info("Fetching names of all the projects matching with name: " + name);
        try {
            return ResponseEntity.ok(projectService.getProjectNames(name));
        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectNames(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching project names");
        }
    }

    @GetMapping("/getIds/{name}")
    public ResponseEntity<?> getProjectIDsByName(@PathVariable String name) {
        logger.info("Fetching corresponding IDs of projects with names matching: " + name);
        try {
            return ResponseEntity.ok(projectService.getProjectIDsByName(name));
        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectIDsByName(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching project IDs");
        }
    }

    @GetMapping("/getProjByNameForDelete/{name}")
    public ResponseEntity<?> getProjectByNameForDelete(@PathVariable String name) {
        logger.info("Fetching projects by name (For deletion) matching with name: " + name);
        try {
            return ResponseEntity.ok(projectService.getProjectByNameForDelete(name));
        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectByNameForDelete(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching projects for deletion");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProject(@RequestBody @Valid Project project, BindingResult result) {
        logger.info("Adding new project with name: " + project.getProjectName());
        if (result.hasErrors()) {
            logger.error("Validation error: " + result.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }

        try {
            projectService.addProject(project);
            logger.info("Project added successfully");
            return ResponseEntity.ok("Project Added Successfully");

        } catch (SQLException e) {
            logger.error("SQLException occurred in addProject(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while adding the project");
        }
    }

    @PutMapping("/updateId/{id}")
    public ResponseEntity<String> updateProjectByID(@PathVariable int id, @RequestBody Project project) {
        logger.info("Updating project by ID with name: " + project.getProjectName());
        try {
            if (!projectService.projectIDExists(id)) {
                logger.warn("Project with ID: '" + id + "' does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Project with ID: '" + id + "' does not exist");
            }

            if (projectService.checkProjectStartDateConflict(id, project.getStartDate())) {
                logger.warn("Update conflict on start date of Project with ID: " + id);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Project Start Date conflicting with active allocations");
            }

            if (projectService.checkProjectEndDateConflict(id, project.getEndDate())) {
                logger.warn("Update conflict on end date of Project with ID: " + id);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Project End Date conflicting with active allocations");
            }

            if (projectService.projectIDAllocationExists(id) && !project.getIsActive()) {
                logger.warn("Project with ID: '" + id + "' has active allocations");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Project with ID: '" + id + "' has active allocations");
            }

            projectService.updateProjectByID(project);
            logger.info("Project with ID: " + id + " updated successfully");
            return ResponseEntity.ok("Project updated successfully");

        } catch (SQLException e) {
            logger.error("SQLException occurred in updateProjectByID(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating the project");
        }
    }

    @DeleteMapping("deleteId/{id}")
    public ResponseEntity<String> deleteProjectByID(@PathVariable int id) {
        logger.info("Deleting project by ID: " + id);
        try {
            if (!projectService.projectIDExists(id)) {
                logger.warn("Project with ID '" + id + "' does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Project with ID '" + id + "' does not exist");
            }

            if (!projectService.projectIDIsActiveCheck(id)) {
                logger.warn("Project with ID '" + id + "' is already inactive");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Project with ID '" + id + "' is already inactive");
            }

            if (projectService.projectIDAllocationExists(id)) {
                logger.warn("Project with ID '" + id + "' has active allocations");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Project with ID '" + id + "' has active allocations");
            }

            projectService.deleteProjectByID(id);
            logger.info("Project with ID: " + id + " deleted successfully");
            return ResponseEntity.ok("Project Deleted Successfully");

        } catch (SQLException e) {
            logger.error("SQLException occurred in deleteProjectByID(...) method of ProjectController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the project");
        }
    }

    @GetMapping("/downloadExcel")
    public void downloadEmployeeExcel(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String value,
            HttpServletResponse response) {
        logger.info("Downloading employee data Excel Sheet");

        List<Project> projects;
        try {
            logger.info("Downloading project data Excel Sheet");

            try {

                if ("byId".equalsIgnoreCase(type) && null != value) {
                    logger.info("Excel Download: Data for Project ID: " + value);
                    Project proj = projectService.getProjectByID(Integer.parseInt(value));
                    projects = proj != null ? List.of(proj) : new ArrayList<>();
                } else if ("byName".equalsIgnoreCase(type) && null != value) {
                    logger.info("Excel Download: Data for Project Name: " + value);
                    projects = projectService.getProjectByName(value);
                } else if ("allActive".equalsIgnoreCase(type)) {
                    logger.info("Excel Download: All Active Projects data");
                    projects = projectService.getAllProjects(true);
                } else {
                    logger.info("Excel Download: All Projects data");
                    projects = projectService.getAllProjects(false);
                }
            } catch (SQLException e) {
                logger.error("SQLException occurred while fetching projects in downloadProjectExcel", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            projectExcelExportService.generateExcelAsync(projects, response);
            logger.info("Excel generation initiated");
        } catch (IOException e) {
            logger.error("IOException occurred in downloadProjectExcel(...) method of ProjectController", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
