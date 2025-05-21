/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.project.service;

import com.spectramd.focus.project.entity.Project;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author raghav.mittal
 */
public interface ProjectExcelExportService {

    void generateExcelAsync(List<Project> projects, HttpServletResponse response) throws IOException;
}
