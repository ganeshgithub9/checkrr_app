package com.example.checkrr.service;

import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.dto.ReportDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.Report;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.ReportNotFoundException;
import com.example.checkrr.projection.CandidateGeneralInfoProjection;
import com.example.checkrr.repository.CandidateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


@Service
public class CustomCandidateService implements CandidateService{

    private ModelMapper mapper;
    private CandidateRepository repository;
    CustomCandidateService(@Autowired ModelMapper mapper,@Autowired CandidateRepository repository){
        this.mapper=mapper;
        this.repository=repository;
    }
    @Override
    public String createCandidate(CandidateDTO request) throws SQLException {
        Candidate candidate=toCandidate(request);
        Report report=new Report();
        candidate.setReport(report);
        candidate.setCreatedAt(LocalDate.now());
        candidate=repository.save(candidate);
        return "Created a candidate with id "+candidate.getId();
    }

    @Override
    public CandidateDTO getCandidateGeneralInfoById(Long candidateId) throws CandidateNotFoundException {
        CandidateGeneralInfoProjection candidateProjection= repository.findCandidateGeneralInfoById(candidateId).orElseThrow(()->new CandidateNotFoundException("Candidate id "+candidateId+" does not exist"));
        return mapper.map(candidateProjection, CandidateDTO.class);
    }

    @Override
    public ReportDTO getReportByCandidateId(Long candidateId) throws ReportNotFoundException {
        return repository.findReportByCandidateId(candidateId).orElseThrow(()->new ReportNotFoundException("Report not found for Candidate id "+candidateId));
    }

    public Candidate getReferenceByCandidateId(Long candidateId){
        return repository.getReferenceById(candidateId);
    }

    @Override
    public Long getReportIdByCandidateId(Long candidateId) throws CandidateNotFoundException {
        return repository.findReportIdById(candidateId).orElseThrow(()->new CandidateNotFoundException("Candidate with id "+candidateId+" does not exist"));
    }

    public Candidate toCandidate(CandidateDTO request){
        return mapper.map(request, Candidate.class);
    }
    public CandidateDTO toCandidateDTO(Candidate candidate){
        return mapper.map(candidate, CandidateDTO.class);
    }

}
