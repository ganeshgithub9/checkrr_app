package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.repository.AdverseActionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;

@Service
public class CustomAdverseActionService implements AdverseActionService{

    CandidateService candidateService;
    ReportService reportService;
    AdverseActionRepository repository;
    CustomAdverseActionService(@Autowired CandidateService candidateService,@Autowired ReportService reportService,@Autowired AdverseActionRepository repository){
        this.candidateService=candidateService;
        this.reportService=reportService;
        this.repository=repository;
    }

    @Transactional
    @Override
    public String createAdverseAction(Long candidateId,AdverseActionDTO adverseActionDTO) throws CandidateNotFoundException, SQLException {
        Long reportId=candidateService.getReportIdByCandidateId(candidateId);
        Candidate candidate=candidateService.getReferenceByCandidateId(candidateId);

        LocalDate today=LocalDate.now();
        AdverseAction adverseAction=new AdverseAction();
        adverseAction.setStatus(AdverseActionStatus.SCHEDULED);
        adverseAction.setPreNoticeDate(today);
        adverseAction.setPostNoticeDate(today.plusDays(adverseActionDTO.getNoticeDays()));
        adverseAction.setCandidate(candidate);
        AdverseAction adverseAction1=repository.save(adverseAction);
        AdjudicationUpdationDTO adjudicationUpdationDTO=toAdjudicationUpdationDTO(reportId,adverseActionDTO);
        reportService.updateAdjudicationDetails(adjudicationUpdationDTO);
        return "Adverse action created with id "+adverseAction1.getId();
    }
    AdjudicationUpdationDTO toAdjudicationUpdationDTO(Long reportId,AdverseActionDTO adverseActionDTO){
        AdjudicationUpdationDTO dto=new AdjudicationUpdationDTO();
        dto.setAdjudication(adverseActionDTO.getAdjudication());
        dto.setCreatedAt(LocalDate.now());
        dto.setCompletedAt(null);
        dto.setStatus(adverseActionDTO.getStatus());
        dto.setReportId(reportId);
        return dto;
    }
}
