/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.allocation.controller;

import com.spectramd.focus.allocation.entity.Allocation;
import com.spectramd.focus.allocation.service.AllocationExcelExportService;
import com.spectramd.focus.allocation.service.AllocationService;
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

/**
 *
 * @author raghav.mittal
 */
@RestController
@RequestMapping("/allocation")
public class AllocationController {

    @Autowired
    private final AllocationService allocationService;

    @Autowired
    private final AllocationExcelExportService allocationExcelExportService;

    private static final Logger logger = Logger.getLogger(AllocationController.class);

    public AllocationController(AllocationService allocationService, AllocationExcelExportService allocationExcelExportService) {
        this.allocationService = allocationService;
        this.allocationExcelExportService = allocationExcelExportService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAllocations(@RequestParam boolean onlyActive) {
        try {
            if (onlyActive) {
                logger.info("Fetching all active allocations");
            } else {
                logger.info("Fetching all allocations");
            }
            return ResponseEntity.ok(allocationService.getAllAllocations(onlyActive));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllAllocations(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocations");
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getByAllocationID(@PathVariable int id) {
        try {
            logger.info("Fetching details of allocation with id:" + id);
            return ResponseEntity.ok(allocationService.getByAllocationID(id));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getByAllocationID(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocation by ID");
        }
    }

    @GetMapping("/getAllocByEmpName/{name}")
    public ResponseEntity<?> getAllAllocationByEmployeeName(@PathVariable String name) {
        try {
            logger.info("Fetching all allocations for Employee name: " + name);
            return ResponseEntity.ok(allocationService.getAllAllocationByEmployeeName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllAllocationByEmployeeName(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocations by employee name");
        }
    }

    @GetMapping("/getAllocDelByEmpName/{name}")
    public ResponseEntity<?> getAllocationForDeleteByEmployeeName(@PathVariable String name) {
        try {
            logger.info("Fetching allocations (for deletion) for Employee name: " + name);
            return ResponseEntity.ok(allocationService.getAllocationForDeleteByEmployeeName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationForDeleteByEmployeeName(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocations for delete by employee name");
        }
    }

    @GetMapping("/getAllocDelByProjName/{name}")
    public ResponseEntity<?> getAllocationForDeleteByProjectName(@PathVariable String name) {
        try {
            logger.info("Fetching allocations (for deletion) for Project name: " + name);
            return ResponseEntity.ok(allocationService.getAllocationForDeleteByProjectName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationForDeleteByProjectName(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocations for delete by project name");
        }
    }

    @GetMapping("/getByProjName/{name}")
    public ResponseEntity<?> getAllocationByProjectName(@PathVariable String name) {
        try {
            logger.info("Fetching allocations for Project name: " + name);
            return ResponseEntity.ok(allocationService.getAllocationByProjectName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationByProjectName(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocations by project name");
        }
    }

    @GetMapping("/getByEmpId/{id}")
    public ResponseEntity<?> getAllocationByEmployeeId(@PathVariable int id) {
        try {
            logger.info("Fetching allocations for Employee ID: " + id);
            return ResponseEntity.ok(allocationService.getByEmployeeID(id));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocationByEmployeeId(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocations by employee ID");
        }
    }

    @GetMapping("/getNames/{name}")
    public ResponseEntity<?> getAllocatedEmployeeNames(@PathVariable String name) {
        try {
            logger.info("Fetching names of employees with allocations matching: " + name);
            return ResponseEntity.ok(allocationService.getAllocatedEmployeeNames(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedEmployeeNames(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching employee names");
        }
    }

    @GetMapping("/getProjects/{name}")
    public ResponseEntity<?> getAllocatedProjectNames(@PathVariable String name) {
        try {
            logger.info("Fetching project names with allocations for Employee name: " + name);
            return ResponseEntity.ok(allocationService.getAllocatedProjectNames(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedProjectNames(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching project names");
        }
    }

    @GetMapping("/getAllocIds/{name}")
    public ResponseEntity<?> getAllocatedIDs(@PathVariable String name) {
        try {
            logger.info("Fetching allocation IDs for Employee name: " + name);
            return ResponseEntity.ok(allocationService.getAllocatedIDs(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedIDs(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocation IDs");
        }
    }

    @GetMapping("/getIds/{name}")
    public ResponseEntity<?> getAllocatedEmployeeIDs(@PathVariable String name) {
        try {
            logger.info("Fetching employee IDs with allocations for name: " + name);
            return ResponseEntity.ok(allocationService.getAllocatedEmployeeIDs(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllocatedEmployeeIDs(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching allocated employee IDs");
        }
    }

    @GetMapping("/getEmpIdByName/{name}")
    public ResponseEntity<?> getEmployeeIDByName(@PathVariable String name) {
        try {
            logger.info("Fetching Employee ID for name: " + name);
            return ResponseEntity.ok(allocationService.getEmployeeIDByName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeIDByName(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching employee ID by name");
        }
    }

    @GetMapping("/getProjIdByName/{name}")
    public ResponseEntity<?> getProjectIDByName(@PathVariable String name) {
        try {
            logger.info("Fetching Project ID for name: " + name);
            return ResponseEntity.ok(allocationService.getProjectIDByName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getProjectIDByName(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching project ID by name");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAllocation(@RequestBody @Valid Allocation allocation, BindingResult result) {
        try {
            logger.info("Adding new allocation for employee with ID: " + allocation.getAssigneeId());
            int assigneeId = allocation.getAssigneeId();
            int projectId = allocation.getProjectId();

            if (!allocationService.employeeIsActiveCheck(assigneeId)) {
                logger.warn("Employee with name '" + allocation.getAssigneeName() + "' is inactive");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee with name '" + allocation.getAssigneeName() + "' is inactive");
            }

            if (!allocationService.projectIsActiveCheck(projectId)) {
                logger.warn("Project with name '" + allocation.getProjectName() + "' is inactive");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Project with name '" + allocation.getProjectName() + "' is inactive");
            }

            String allocationStartDate = allocation.getAllocationStartDate();
            String allocationEndDate = allocation.getAllocationEndDate();

            if (!allocationService.employeeJoiningDateCheck(allocationStartDate, assigneeId)) {
                logger.warn("Allocation start date is before employee joining date");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Allocation start date cannot be before employee joining date");
            }

            if (!allocationService.projectDatesCheck(allocationStartDate, allocationEndDate, projectId)) {
                logger.warn("Allocation dates are not within project start and end dates");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Allocation dates should be within the project start and end dates");
            }

            if (allocationService.datesOverlapCheckForActiveAllocation(assigneeId, allocationStartDate, allocationEndDate)) {
                logger.warn("Existing active allocation for Employee ID: '" + assigneeId + "' in entered dates");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("An active allocation already exists with overlapping dates for this employee");
            }

            if (allocationService.isAllocationExistsForSameProject(assigneeId, projectId)) {
                logger.warn("Entered employee is already allocated to entered project");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee already allocated to the project");
            }

            if (result.hasErrors()) {
                logger.error("Validation error: " + result.getFieldError().getDefaultMessage());
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }

            allocationService.addAllocation(allocation);
            logger.info("Allocation added successfully");
            return ResponseEntity.ok("Allocation added successfully");

        } catch (SQLException e) {
            logger.error("SQLException occured in addAllocation(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while adding new allocation");
        }
    }

    @PutMapping("/updateId/{id}")
    public ResponseEntity<String> updateAllocation(@PathVariable int id, @RequestBody Allocation allocation) {
        try {
            logger.info("Updating allocation for employee with name: '" + allocation.getAssigneeName() + "' by allocation ID");

            if (!allocationService.allocationIDExists(id)) {
                logger.warn("Allocation with ID: '" + id + "' does not exists");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Allocation with ID: '" + id + "' does not exists");
            }

            logger.debug("Fetching Employee ID for the employee name entered (updating allocation)");
            int assigneeId = allocationService.getEmployeeIDByName(allocation.getAssigneeName());

            logger.debug("Fetching Project ID for the project name entered (updating allocation)");
            int projectId = allocationService.getProjectIDByName(allocation.getProjectName());

            if (!allocationService.employeeIsActiveCheck(assigneeId)) {
                logger.warn("Employee with name '" + allocation.getAssigneeName() + "' is inactive");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee with name '" + allocation.getAssigneeName() + "' is inactive");
            }

            if (!allocationService.projectIsActiveCheck(projectId)) {
                logger.warn("Project with name '" + allocation.getProjectName() + "' is inactive");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Project with name '" + allocation.getProjectName() + "' is inactive");
            }

            if (!allocationService.employeeJoiningDateCheck(allocation.getAllocationStartDate(), allocation.getAssigneeId())) {
                logger.warn("Allocation start date is before employee joining date");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Allocation start date cannot be before employee joining date");
            }

            if (!allocationService.projectDatesCheck(allocation.getAllocationStartDate(), allocation.getAllocationEndDate(), allocation.getProjectId())) {
                logger.warn("Allocation dates are not within project start and end dates");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Allocation dates should be within the project start and end dates");
            }

            if (allocationService.datesOverlapCheckForActiveAllocation(allocation.getAssigneeId(), allocation.getAllocationStartDate(), allocation.getAllocationEndDate())) {
                logger.warn("Existing active allocation for Employee ID: '" + assigneeId + "' in entered dates");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("An active allocation already exists with overlapping dates for this employee");
            }

            allocation.setAllocationId(id);
            allocationService.updateAllocation(allocation);
            logger.info("Allocation with ID: " + id + " updated successfully");
            return ResponseEntity.ok("Allocation updated successfully");

        } catch (SQLException e) {
            logger.error("SQLException occured in updateAllocation(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating allocation by ID");
        }
    }

    @DeleteMapping("deleteId/{id}")
    public ResponseEntity<String> deleteByAllocationID(@PathVariable int id) {
        try {
            logger.info("Deleting allocation by ID: " + id);
            if (!allocationService.allocationIDExists(id)) {
                logger.warn("Allocation with ID '" + id + "' does not exists");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Allocation with ID '" + id + "' does not exists");
            }

            allocationService.deleteByAllocationID(id);
            logger.info("Allocation with ID: " + id + " deleted successfully");
            return ResponseEntity.ok("Allocation Deleted Successfully");

        } catch (SQLException e) {
            logger.error("SQLException occured in deleteByAllocationID(...) method of AllocationController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting allocation by ID");
        }
    }

    @GetMapping("/downloadExcel")
    public void downloadEmployeeExcel(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String value,
            HttpServletResponse response) {
        logger.info("Downloading employee data Excel Sheet");

        List<Allocation> allocations;

        try {
            logger.info("Downloading allocation data Excel Sheet");

            try {

                if ("byId".equalsIgnoreCase(type) && null != value) {
                    logger.info("Excel Download: Data for Allocation ID: " + value);
                    Allocation alloc = allocationService.getByAllocationID(Integer.parseInt(value));
                    allocations = alloc != null ? List.of(alloc) : new ArrayList<>();
                } else if ("byEmployeeName".equalsIgnoreCase(type) && null != value) {
                    logger.info("Excel Download: Data for Employee Name: " + value);
                    allocations = allocationService.getAllAllocationByEmployeeName(value);
                } else if ("byProjectName".equalsIgnoreCase(type) && null != value) {
                    logger.info("Excel Download: Data for Project Name: " + value);
                    allocations = allocationService.getAllocationByProjectName(value);
                } else if ("allActive".equalsIgnoreCase(type)) {
                    logger.info("Excel Download: All Active Allocations data");
                    allocations = allocationService.getAllAllocations(true);
                } else {
                    logger.info("Excel Download: All Allocations data");
                    allocations = allocationService.getAllAllocations(false);
                }
            } catch (SQLException e) {
                logger.error("SQLException occurred while fetching allocations in downloadAllocationtExcel", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            allocationExcelExportService.generateExcelAsync(allocations, response);
            logger.info("Excel generation initiated");
        } catch (IOException e) {
            logger.error("IOException occurred in downloadAllocationExcel(...) method of AllocationController", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
