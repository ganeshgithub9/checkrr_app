package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    Report getReferenceById(Long reportId);
    void saveReport(Report report);

    int updateAdjudicationDetails(AdjudicationUpdationDTO adjudicationUpdationDTO);

    Page<CandidateWithReportDTO> getCandidatesWithReports(String name, Adjudication adjudication, Status status, Pageable pageable);

    List<CandidateWithReportDTO> getCandidatesWithReportsByDateRange(ExportDTO exportDTO);
}
