/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.allocation.service;

import com.spectramd.focus.allocation.entity.Allocation;
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
public class AllocationExcelExportServiceImpl implements AllocationExcelExportService {

    private static final Logger logger = Logger.getLogger(AllocationExcelExportServiceImpl.class);
    
    @Async("taskExecutor")
    @Override
    public void generateExcelAsync(List<Allocation> allocations, HttpServletResponse response) throws IOException {
        
        logger.info("Entering generateExcelAsync(...) method of AllocationExcelExportServiceImpl");

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Project");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Allocation ID");
            header.createCell(1).setCellValue("Employee Name");
            header.createCell(2).setCellValue("Project Name");
            header.createCell(3).setCellValue("Start Date");
            header.createCell(4).setCellValue("End Date");
            header.createCell(5).setCellValue("Allocated By");
            header.createCell(6).setCellValue("% Allocation");
            header.createCell(7).setCellValue("Status");

            int rowIdx = 1;
            for (Allocation alloc : allocations) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(alloc.getAllocationId());
                row.createCell(1).setCellValue(alloc.getAssigneeName());
                row.createCell(2).setCellValue(alloc.getProjectName());
                row.createCell(3).setCellValue(alloc.getAllocationStartDate());
                row.createCell(4).setCellValue(alloc.getAllocationEndDate());
                row.createCell(5).setCellValue(alloc.getAllocatorName());
                row.createCell(6).setCellValue(alloc.getPercentageAllocation());
                row.createCell(7).setCellValue(alloc.getIsActive() ? "Active" : "Inactive");

            }

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

            workbook.write(response.getOutputStream());
            response.flushBuffer();
            
            logger.info("Exit generateExcelAsync(...) method of AllocationExcelExportServiceImpl");
        } catch (IOException e) {
            logger.error("IOException occured in generateExcelAsync(...) of AllocationExcelExportServiceImpl");
            throw new IOException("IOException occured in generateExcelAsync(...) of EmployeeExcelExportServiceImpl",e);
        }
    }

}
