package com.example.checkrr.service;

import com.example.checkrr.dto.CourtSearchDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.CourtSearch;
import com.example.checkrr.exceptions.CourtSearchNotFoundException;
import com.example.checkrr.projection.CourtSearchProjection;
import com.example.checkrr.repository.CourtSearchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

@Service
public class CustomCourtSearchService implements CourtSearchService{

    private CandidateService candidateService;
    private CourtSearchRepository repository;
    private ModelMapper mapper;

    CustomCourtSearchService(@Autowired CourtSearchRepository repository,@Autowired CandidateService candidateService,@Autowired ModelMapper mapper){
        this.repository=repository;
        this.candidateService=candidateService;
        this.mapper=mapper;
    }

    @Override
    public String createCourtSearch(Long candidateId, CourtSearchDTO courtSearchDTO) throws SQLException {
        Candidate candidate=candidateService.getReferenceByCandidateId(candidateId);
        CourtSearch courtSearch=mapper.map(courtSearchDTO, CourtSearch.class);
        courtSearch.setCandidate(candidate);
        courtSearch.setReportedAt(LocalDate.now());
        courtSearch=repository.save(courtSearch);
        return "Created CourtSearch with id "+courtSearch.getId();
    }

    @Override
    public List<CourtSearchProjection> getCourtSearchDetailsByCandidateId(Long candidateId) throws CourtSearchNotFoundException {
        return repository.getCourtSearchDetailsByCandidateId(candidateId).orElseThrow(()->new CourtSearchNotFoundException("No CourtSearch found for the candidate id "+candidateId));
    }
}
