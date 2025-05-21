package com.spectramd.focus.employee.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Integer employeeId;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@staff\\.com$", message = "Email must end with '@staff.com'")
    private String email;

    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "Date must be in format YYYY-MM-DD")
    private String dateOfJoining;

    @Min(value = 1, message = "Role ID must be a positive integer")
    private Integer roleId;

    private String roleName;

    private Boolean isActive;

    private Boolean isAdmin;
}
