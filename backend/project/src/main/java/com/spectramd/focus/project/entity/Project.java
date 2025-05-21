package com.spectramd.focus.project.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Integer projectId;

    @Size(min = 2, max = 100, message = "Project name must be between 2 and 100 characters")
    private String projectName;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Min(value = 1, message = "Project owner ID must be a positive integer")
    private Integer projectOwnerId;

    private String projectOwnerName;

    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "Start date must be in format YYYY-MM-DD"
    )
    private String startDate;

    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "End date must be in format YYYY-MM-DD"
    )
    private String endDate;

    private Boolean isActive;
}
