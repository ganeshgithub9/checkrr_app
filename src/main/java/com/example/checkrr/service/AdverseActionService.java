package com.example.checkrr.service;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.exceptions.CandidateNotFoundException;

import java.sql.SQLException;

public interface AdverseActionService {
    String createAdverseAction(Long candidateId,AdverseActionDTO adverseActionDTO) throws CandidateNotFoundException, SQLException;
}
