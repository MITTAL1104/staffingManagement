/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.allocation.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author raghav.mittal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allocation {

    private Integer allocationId;
    private Integer assigneeId;

    @Size(min = 2, max = 100, message = "Assignee name must be between 2 and 100 characters")
    private String assigneeName;

    private Integer projectId;

    @Size(min = 2, max = 100, message = "Project name must be between 2 and 100 characters")
    private String projectName;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Start date must be in format YYYY-MM-DD")
    private String allocationStartDate;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "End date must be in format YYYY-MM-DD")
    private String allocationEndDate;

    @Size(min = 2, max = 100, message = "Allocator name must be between 2 and 100 characters")
    private String allocatorName;

    @Min(value = 100, message = "Percentage must be exactly 100")
    @Max(value = 100, message = "Percentage must be exactly 100")
    private Integer percentageAllocation = 100;

    private Boolean isActive;
}


