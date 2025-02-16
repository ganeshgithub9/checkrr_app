package com.example.checkrr.service;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.dto.EmailMetaData;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.FileUploadFailedException;
import com.example.checkrr.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

public interface AdverseActionService {
    String createAdverseAction(Long candidateId,AdverseActionDTO adverseActionDTO) throws CandidateNotFoundException, SQLException;

    Page<AdverseActionResponseDTO> getAdverseActions(String name, AdverseActionStatus status, Pageable pageable);

    String createAdverseActionWithMailAndAttachments(Long candidateId, @Valid AdverseActionDTO adverseActionDTO, @Valid EmailMetaData emailMetaData, MultipartFile[] files) throws CandidateNotFoundException, UserNotFoundException, FileUploadFailedException;
}
