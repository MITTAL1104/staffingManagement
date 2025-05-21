/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spectramd.focus.allocation.service;

import com.spectramd.focus.allocation.entity.Allocation;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author raghav.mittal
 */
public interface AllocationExcelExportService {

    public void generateExcelAsync(List<Allocation> allocations, HttpServletResponse response) throws IOException;
}
