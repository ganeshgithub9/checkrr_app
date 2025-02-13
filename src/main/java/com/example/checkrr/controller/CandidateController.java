package com.example.checkrr.controller;

import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.dto.ReportDTO;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.ReportNotFoundException;
import com.example.checkrr.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {
    private CandidateService candidateService;
    CandidateController(@Autowired CandidateService candidateService){
        this.candidateService=candidateService;
    }

    @PostMapping
    public ResponseEntity<String> createCandidate(@Valid @RequestBody CandidateDTO request) throws SQLException {
        return new ResponseEntity<>(candidateService.createCandidate(request), HttpStatus.CREATED);
    }

    @GetMapping("/{candidate-id}")
    public ResponseEntity<CandidateDTO> getCandidateGeneralInfoById(@PathVariable("candidate-id") Long candidateId) throws CandidateNotFoundException {
        return new ResponseEntity<>(candidateService.getCandidateGeneralInfoById(candidateId),HttpStatus.OK);
    }

    @GetMapping("/{candidate-id}/report")
    public ResponseEntity<ReportDTO> getReportByCandidateId(@PathVariable("candidate-id") Long candidateId) throws ReportNotFoundException {
        return new ResponseEntity<>(candidateService.getReportByCandidateId(candidateId),HttpStatus.OK);
    }
}
