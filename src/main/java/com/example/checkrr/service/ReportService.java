package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.entity.Report;

public interface ReportService {
    Report getReferenceById(Long reportId);
    void saveReport(Report report);

    int updateAdjudicationDetails(AdjudicationUpdationDTO adjudicationUpdationDTO);
}
