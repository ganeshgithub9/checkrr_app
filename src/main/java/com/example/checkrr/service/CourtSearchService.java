package com.example.checkrr.service;

import com.example.checkrr.dto.CourtSearchDTO;
import com.example.checkrr.exceptions.CourtSearchNotFoundException;
import com.example.checkrr.projection.CourtSearchProjection;

import java.sql.SQLException;
import java.util.List;

public interface CourtSearchService {
    String createCourtSearch(Long candidateId, CourtSearchDTO courtSearchDTO) throws SQLException;

    List<CourtSearchProjection> getCourtSearchDetailsByCandidateId(Long candidateId) throws CourtSearchNotFoundException;
}
