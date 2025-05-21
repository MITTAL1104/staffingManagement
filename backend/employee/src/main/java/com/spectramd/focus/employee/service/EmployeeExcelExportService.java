/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.employee.service;

import com.spectramd.focus.employee.entity.Employee;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author raghav.mittal
 */
public interface EmployeeExcelExportService {

    void generateExcelAsync(List<Employee> employees, HttpServletResponse response) throws IOException;
}
