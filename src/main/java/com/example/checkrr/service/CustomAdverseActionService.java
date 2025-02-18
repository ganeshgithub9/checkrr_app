package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.dto.EmailMetaData;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.User;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.FileUploadFailedException;
import com.example.checkrr.exceptions.UserNotFoundException;
import com.example.checkrr.repository.AdverseActionRepository;
import com.example.checkrr.specifications.AdverseActionSpecification;
import com.example.checkrr.util.AttachmentUtil;
import com.example.checkrr.util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomAdverseActionService implements AdverseActionService{

    CandidateService candidateService;
    UserService userService;
    ReportService reportService;
    AdverseActionRepository repository;
    private final ModelMapper modelMapper;
    AttachmentUtil attachmentUtil;
    EmailUtil emailUtil;

    CustomAdverseActionService(@Autowired CandidateService candidateService,@Autowired UserService userService,@Autowired ReportService reportService,@Autowired AdverseActionRepository repository,
                               ModelMapper modelMapper, @Autowired AttachmentUtil attachmentUtil,@Autowired EmailUtil emailUtil){
        this.candidateService=candidateService;
        this.userService=userService;
        this.reportService=reportService;
        this.repository=repository;
        this.modelMapper = modelMapper;
        this.attachmentUtil=attachmentUtil;
        this.emailUtil=emailUtil;
    }


    @Override
    public Page<AdverseActionResponseDTO> getAdverseActions(String name, AdverseActionStatus status, Pageable pageable) {
        Specification<AdverseAction> adverseActionSpecification= Specification.where(AdverseActionSpecification.hasNameLike(name).and(AdverseActionSpecification.hasStatus(status)));

        Page<AdverseAction> pageOfAdverseActions=repository.findAll(adverseActionSpecification,pageable);
        return pageOfAdverseActions.map(adverseAction -> modelMapper.map(adverseAction, AdverseActionResponseDTO.class));
    }

    @Transactional
    @Override
    public Long createAdverseActionWithMailAndAttachments(Long candidateId, AdverseActionDTO adverseActionDTO, EmailMetaData emailMetaData, MultipartFile[] files) throws CandidateNotFoundException, UserNotFoundException, FileUploadFailedException, MessagingException {
        Long reportId=candidateService.getReportIdByCandidateId(candidateId);
        Candidate candidate=candidateService.getReferenceByCandidateId(candidateId);
        User user= Optional.ofNullable(userService.getUserReferenceById(emailMetaData.getSenderId())).orElseThrow(()->new UserNotFoundException("User with id "+emailMetaData.getSenderId()+" does not exist"));
        LocalDate today=LocalDate.now();

        List<String> attachmentUrls= attachmentUtil.storeAttachmentAndGetURLs(files,candidateId);

        AdverseAction adverseAction=new AdverseAction(null,today,today.plusDays(adverseActionDTO.getNoticeDays()),candidate,AdverseActionStatus.SCHEDULED,emailMetaData.getSubject(),emailMetaData.getBodyInHtml(),user,attachmentUrls);

        AdverseAction adverseAction1=repository.save(adverseAction);

        log.info("Created an adverse action with  id {} for candidate {}",adverseAction1.getId(),adverseAction1.getCandidate().getName());

        AdjudicationUpdationDTO adjudicationUpdationDTO=toAdjudicationUpdationDTO(reportId,adverseActionDTO);

        reportService.updateAdjudicationDetails(adjudicationUpdationDTO);

        String userMailId=userService.getEmailById(emailMetaData.getSenderId());
        String candidateMailId=candidateService.getEmailById(candidateId);

        emailUtil.sendMailWithAttachments(userMailId,candidateMailId,emailMetaData.getSubject(),emailMetaData.getBodyInHtml(),files);
        return adverseAction1.getId();
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
