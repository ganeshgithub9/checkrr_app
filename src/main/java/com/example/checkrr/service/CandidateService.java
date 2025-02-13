package com.example.checkrr.service;

import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.dto.ReportDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.ReportNotFoundException;

import java.sql.SQLException;


public interface CandidateService {
    String createCandidate(CandidateDTO request) throws SQLException;

    CandidateDTO getCandidateGeneralInfoById(Long candidateId) throws CandidateNotFoundException;

    ReportDTO getReportByCandidateId(Long candidateId) throws ReportNotFoundException;

    Candidate getReferenceByCandidateId(Long candidateId);

    Long getReportIdByCandidateId(Long candidateId) throws CandidateNotFoundException;
}
