package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.entity.Report;
import com.example.checkrr.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomReportService implements ReportService{

    ReportRepository repository;

    CustomReportService(@Autowired ReportRepository repository){
        this.repository=repository;
    }

    @Override
    public Report getReferenceById(Long reportId) {
        return repository.getReferenceById(reportId);
    }

    @Override
    public void saveReport(Report report) {
        repository.save(report);
    }

    @Override
    public int updateAdjudicationDetails(AdjudicationUpdationDTO adjudicationUpdationDTO) {
        return repository.updateAdjudicationDetails(adjudicationUpdationDTO);
    }
}
