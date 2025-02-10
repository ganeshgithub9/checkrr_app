package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.repository.AdverseActionRepository;
import com.example.checkrr.specifications.AdverseActionSpecification;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;

@Service
public class CustomAdverseActionService implements AdverseActionService{

    CandidateService candidateService;
    ReportService reportService;
    AdverseActionRepository repository;
    private final ModelMapper modelMapper;

    CustomAdverseActionService(@Autowired CandidateService candidateService,@Autowired ReportService reportService,@Autowired AdverseActionRepository repository,
                               ModelMapper modelMapper){
        this.candidateService=candidateService;
        this.reportService=reportService;
        this.repository=repository;
        this.modelMapper = modelMapper;
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

    @Override
    public Page<AdverseActionResponseDTO> getAdverseActions(String name, AdverseActionStatus status, Pageable pageable) {
        Specification<AdverseAction> adverseActionSpecification= Specification.where(AdverseActionSpecification.hasNameLike(name).and(AdverseActionSpecification.hasStatus(status)));

        Page<AdverseAction> pageOfAdverseActions=repository.findAll(adverseActionSpecification,pageable);
        return pageOfAdverseActions.map(adverseAction -> modelMapper.map(adverseAction, AdverseActionResponseDTO.class));
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
