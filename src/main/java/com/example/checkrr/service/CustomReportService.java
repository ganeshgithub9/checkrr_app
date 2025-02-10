package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.repository.ReportRepository;
import com.example.checkrr.specifications.ReportSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomReportService implements ReportService{

    ReportRepository repository;
    ModelMapper modelMapper;

    CustomReportService(@Autowired ReportRepository repository, @Autowired ModelMapper modelMapper){
        this.repository=repository;
        this.modelMapper=modelMapper;
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

    @Override
    public Page<CandidateWithReportDTO> getCandidatesWithReports(String name, Adjudication adjudication, Status status, Pageable pageable) {

        Specification<Report> reportSpecification=Specification.where(ReportSpecification.hasNameLike(name).and(ReportSpecification.hasAdjudication(adjudication)).and(ReportSpecification.hasAdjudicationStatus(status)));
        Page<Report> page=repository.findAll(reportSpecification,pageable);
        return page.map(report -> modelMapper.map(report, CandidateWithReportDTO.class));
    }

    @Override
    public List<CandidateWithReportDTO> getCandidatesWithReportsByDateRange(ExportDTO exportDTO) {
        Specification<Report> reportSpecification=Specification.where(ReportSpecification.hasDateRange(exportDTO));
        List<Report> list=repository.findAll(reportSpecification);
        return list.stream().map(element -> modelMapper.map(element, CandidateWithReportDTO.class)).toList();
    }
}
