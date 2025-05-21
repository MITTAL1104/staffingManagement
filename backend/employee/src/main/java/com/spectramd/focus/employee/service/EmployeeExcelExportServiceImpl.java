/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.employee.service;

import com.spectramd.focus.employee.entity.Employee;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author raghav.mittal
 */
@Service
public class EmployeeExcelExportServiceImpl implements EmployeeExcelExportService {

    private static final Logger logger = Logger.getLogger(EmployeeExcelExportServiceImpl.class);

    @Async("taskExecutor")
    @Override
    public void generateExcelAsync(List<Employee> employees, HttpServletResponse response) throws IOException {

        logger.info("Entering generateExcelAsync(...) method of EmployeeExcelExportServiceImpl");

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Employee");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Employee ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Date of Joining");
            header.createCell(4).setCellValue("Role");
            header.createCell(5).setCellValue("Status");
            header.createCell(6).setCellValue("Admin");

            int rowIdx = 1;
            for (Employee emp : employees) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(emp.getEmployeeId());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getEmail());
                row.createCell(3).setCellValue(emp.getDateOfJoining());
                row.createCell(4).setCellValue(emp.getRoleName());
                row.createCell(5).setCellValue(emp.getIsActive() ? "Active" : "Inactive");
                row.createCell(6).setCellValue(emp.getIsAdmin() ? "Yes" : "No");
            }

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

            workbook.write(response.getOutputStream());
            response.flushBuffer();

            logger.info("Exit generateExcelAsync(...) method of EmployeeExcelExportServiceImpl");
        } catch (IOException e) {
            logger.error("IOException occured in generateExcelAsync(...) of EmployeeExcelExportServiceImpl", e);
            throw new IOException("IOException occured in generateExcelAsync(...) of EmployeeExcelExportServiceImpl");
        }
    }

}
