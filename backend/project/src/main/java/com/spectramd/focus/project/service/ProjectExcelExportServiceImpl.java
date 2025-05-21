/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.project.service;

import com.spectramd.focus.project.entity.Project;
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
public class ProjectExcelExportServiceImpl implements ProjectExcelExportService {
    
    private static final Logger logger = Logger.getLogger(ProjectExcelExportServiceImpl.class);

    @Async("taskExecutor")
    @Override
    public void generateExcelAsync(List<Project> projects, HttpServletResponse response) throws IOException {

        logger.info("Entering generateExcelAsync(...) method of ProjectExcelExportServiceImpl");

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Project");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Project ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Description");
            header.createCell(3).setCellValue("Project Owner Name");
            header.createCell(4).setCellValue("Start Date");
            header.createCell(5).setCellValue("End Date");
            header.createCell(6).setCellValue("Status");

            int rowIdx = 1;
            for (Project proj : projects) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(proj.getProjectId());
                row.createCell(1).setCellValue(proj.getProjectName());
                row.createCell(2).setCellValue(proj.getDescription());
                row.createCell(3).setCellValue(proj.getProjectOwnerName());
                row.createCell(4).setCellValue(proj.getStartDate());
                row.createCell(5).setCellValue(proj.getEndDate());
                row.createCell(5).setCellValue(proj.getIsActive() ? "Active" : "Inactive");

            }

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

            workbook.write(response.getOutputStream());
            response.flushBuffer();
            
            logger.info("Exit generateExcelAsync(...) method of ProjectExcelExportServiceImpl");
        } catch (IOException e) {
            logger.error("IOException occured in generateExcelAsync(...) of ProjectExcelExportServiceImpl",e);
            throw new IOException("IOException occured in generateExcelAsync(...) of ProjectExcelExportServiceImpl");
        }
    }

}
