package com.example.checkrr.service;

import com.example.checkrr.dto.CourtSearchDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.CourtSearch;
import com.example.checkrr.exceptions.CourtSearchNotFoundException;
import com.example.checkrr.projection.CourtSearchProjection;
import com.example.checkrr.repository.CourtSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
@Slf4j
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
    public Long createCourtSearch(Long candidateId, CourtSearchDTO courtSearchDTO) throws SQLException {
        Candidate candidate=candidateService.getReferenceByCandidateId(candidateId);
        CourtSearch courtSearch=mapper.map(courtSearchDTO, CourtSearch.class);
        courtSearch.setCandidate(candidate);
        courtSearch.setReportedAt(LocalDate.now());
        courtSearch=repository.save(courtSearch);
        log.info("Created a court-search with id {} for candidate id {}",courtSearch.getId(),courtSearch.getCandidate().getId());
        return courtSearch.getId();
    }

    @Override
    public List<CourtSearchProjection> getCourtSearchDetailsByCandidateId(Long candidateId) throws CourtSearchNotFoundException {
        return repository.getCourtSearchDetailsByCandidateId(candidateId).orElseThrow(()->new CourtSearchNotFoundException("No CourtSearch found for the candidate id "+candidateId));
    }
}
