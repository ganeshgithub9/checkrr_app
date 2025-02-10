package com.example.checkrr.service;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;

public interface AdverseActionService {
    String createAdverseAction(Long candidateId,AdverseActionDTO adverseActionDTO) throws CandidateNotFoundException, SQLException;

    Page<AdverseActionResponseDTO> getAdverseActions(String name, AdverseActionStatus status, Pageable pageable);
}
