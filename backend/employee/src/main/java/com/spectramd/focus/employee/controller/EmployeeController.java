package com.spectramd.focus.employee.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spectramd.focus.employee.entity.Employee;
import com.spectramd.focus.employee.service.EmployeeService;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import com.spectramd.focus.employee.service.EmployeeExcelExportService;
import java.io.IOException;
import java.sql.SQLException;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final EmployeeExcelExportService employeeExcelExportService;

    private static final Logger logger = Logger.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService, EmployeeExcelExportService employeeExcelExportService) {
        this.employeeService = employeeService;
        this.employeeExcelExportService = employeeExcelExportService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmployees(@RequestParam boolean onlyActive) {
        try {
            if (onlyActive) {
                logger.info("Fetching all active employees");
            } else {
                logger.info("Fetching all employees");
            }

            List<Employee> employees = employeeService.getAllEmployees(onlyActive);
            return ResponseEntity.ok(employees);

        } catch (SQLException e) {
            logger.error("SQlException occured in getAllEmployees(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured while fetching employees");
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getEmployeeByID(@PathVariable int id) {
        try {
            logger.info("Fetching details of employee with id:" + id);
            return ResponseEntity.ok(employeeService.getEmployeeByID(id));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeByID(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured while fetching employee By ID");
        }
    }

    @GetMapping("/getAllByName/{name}")
    public ResponseEntity<?> getEmployeeByName(@PathVariable String name) {
        try {
            logger.info("Fetching details of all employees with matching name: " + name);
            return ResponseEntity.ok(employeeService.getEmployeeByName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeByName(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching employees by name");
        }
    }

    @GetMapping("/getNames/{name}")
    public ResponseEntity<?> getEmployeeNames(@PathVariable String name) {
        try {
            logger.info("Fetching names of all the employees matching with name: " + name);
            return ResponseEntity.ok(employeeService.getEmployeeNames(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeNames(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching employee names by name");
        }
    }

    @GetMapping("/getAllNames")
    public ResponseEntity<?> getAllEmployeeNames() {
        try {
            logger.info("Fetching names of all the employees at particular role");
            return ResponseEntity.ok(employeeService.getAllEmployeeNames());

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllEmployeeNames(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching all employee names");
        }
    }

    @GetMapping("/getIds/{name}")
    public ResponseEntity<?> getEmployeeIDsByName(@PathVariable String name) {
        try {
            logger.info("Fetching corresponding IDs of employee with names matching with name: " + name);
            return ResponseEntity.ok(employeeService.getEmployeeIDsByName(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeIDsByName(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching employee IDs by name");
        }
    }

    @GetMapping("/getNameByEmail/{email}")
    public ResponseEntity<?> getEmployeeNameByEmail(@PathVariable String email) {
        try {
            logger.info("Fetching names of the employee with email ID: " + email);
            return ResponseEntity.ok(employeeService.getEmployeeNameByEmail(email));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeNameByEmail(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Eror occurred while fetching employee name by email");
        }
    }

    @GetMapping("/getRoles")
    public ResponseEntity<?> getAllRoles() {
        try {
            logger.info("Fetching all the existing roles");
            return ResponseEntity.ok(employeeService.getAllRoles());

        } catch (SQLException e) {
            logger.error("SQLException occurred in getAllRoles() method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching all roles");
        }
    }

    @GetMapping("/getEmpByNameForDelete/{name}")
    public ResponseEntity<?> getEmployeeByNameForDelete(@PathVariable String name) {
        try {
            logger.info("Fetching employees by name (For deletion) matching with name: " + name);
            return ResponseEntity.ok(employeeService.getEmployeeByNameForDelete(name));

        } catch (SQLException e) {
            logger.error("SQLException occurred in getEmployeeByNameForDelete(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching employees for deletion by name");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation error: " + result.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }

        try {
            logger.info("Adding new employee with name: " + employee.getName());
            employeeService.addEmployee(employee);
            logger.info("Employee added successfully");
            return ResponseEntity.ok("Employee added successfully");

        } catch (SQLException e) {
            logger.error("SQLException occurred in addEmployee(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while adding the employee");
        }
    }

    @PutMapping("/updateId/{id}")
    public ResponseEntity<String> updateEmployeeByID(@PathVariable int id, @RequestBody Employee employee) {
        try {
            logger.info("Updating employee by ID with name: " + employee.getName());

            if (!employeeService.employeeIDExists(id)) {
                logger.warn("Employee with ID: '" + id + "' does not exists");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee with ID '" + id + "' does not exists");
            }

            if (employeeService.checkDateOfJoiningConflict(id, employee.getDateOfJoining())) {
                logger.warn("Update conflict for Employee with ID: " + id);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Date of Joining conflicting with active allocation of the employee");
            }

            if (employeeService.employeeIDAllocationExists(id) && !employee.getIsActive()) {
                logger.warn("Employee with ID: " + id + " has active allocations");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Employee with ID: '" + id + "' has active allocations");
            }

            employeeService.updateEmployeeByID(employee);
            logger.info("Employee with ID: " + id + " updated successfully");
            return ResponseEntity.ok("Employee updated successfully");

        } catch (SQLException e) {
            logger.error("SQLException occurred in updateEmployeeByID(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating the employee");
        }
    }

    @DeleteMapping("/deleteId/{id}")
    public ResponseEntity<String> deleteEmployeeByID(@PathVariable int id) {
        try {
            logger.info("Deleting employee by ID: " + id);

            if (!employeeService.employeeIDExists(id)) {
                logger.warn("Employee with ID '" + id + "' does not exists");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee with ID '" + id + "' does not exists");
            }

            if (!employeeService.employeeIDIsActiveCheck(id)) {
                logger.warn("Employee with ID '" + id + "' is already inactive");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Employee with ID '" + id + "' is already inactive");
            }

            if (employeeService.employeeIDAllocationExists(id)) {
                logger.warn("Employee with ID '" + id + "' has active allocations");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Employee with ID '" + id + "' has active allocations");
            }

            if (employeeService.checkActiveProjectOwner(id)) {
                logger.warn("Employee with ID '" + id + "' is owner of an active project");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Employee with ID '" + id + "' is owner of an active project");
            }

            employeeService.deleteEmployeeByID(id);
            logger.info("Employee with ID: " + id + " deleted successfully");
            return ResponseEntity.ok("Employee Deleted Successfully");

        } catch (SQLException e) {
            logger.error("SQLException occurred in deleteEmployeeByID(...) method of EmployeeController", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/downloadExcel")
    public void downloadEmployeeExcel(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String value,
            HttpServletResponse response) {

        List<Employee> employees = new ArrayList<>();

        try {
            logger.info("Downloading employee data Excel Sheet");

            try {
                if ("byId".equalsIgnoreCase(type) && value != null) {
                    logger.info("Excel Download: Data for Employee ID: " + value);
                    Employee emp = employeeService.getEmployeeByID(Integer.parseInt(value));
                    employees = emp != null ? List.of(emp) : new ArrayList<>();
                } else if ("byName".equalsIgnoreCase(type) && value != null) {
                    logger.info("Excel Download: Data for Employee Name: " + value);
                    employees = employeeService.getEmployeeByName(value);
                } else if ("allActive".equalsIgnoreCase(type)) {
                    logger.info("Excel Download: All Active Employees data");
                    employees = employeeService.getAllEmployees(true);
                } else {
                    logger.info("Excel Download: All Employees data");
                    employees = employeeService.getAllEmployees(false);
                }
            } catch (SQLException e) {
                logger.error("SQLException occurred while fetching employees in downloadEmployeeExcel", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;  
            }

            employeeExcelExportService.generateExcelAsync(employees, response);
            logger.info("Excel generation initiated");

        } catch (IOException e) {
            logger.error("IOException occurred in downloadEmployeeExcel(...) method of EmployeeController", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
