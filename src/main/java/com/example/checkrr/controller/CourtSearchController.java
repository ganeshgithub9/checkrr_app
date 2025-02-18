package com.example.checkrr.controller;

import com.example.checkrr.dto.CourtSearchDTO;
import com.example.checkrr.exceptions.CourtSearchNotFoundException;
import com.example.checkrr.projection.CourtSearchProjection;
import com.example.checkrr.service.CourtSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates/{candidate-id}/court-searches")
public class CourtSearchController {

    CourtSearchService courtSearchService;
    CourtSearchController(@Autowired CourtSearchService courtSearchService){
        this.courtSearchService=courtSearchService;
    }
    @PostMapping
    public ResponseEntity<String> createCourtSearch(@PathVariable("candidate-id") Long candidateId, @Valid @RequestBody CourtSearchDTO courtSearchDTO) throws SQLException {
        Long courtSearchId=courtSearchService.createCourtSearch(candidateId,courtSearchDTO);
        return new ResponseEntity<>("Created CourtSearch with id "+courtSearchId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourtSearchProjection>> getCourtSearchDetailsByCandidateId(@PathVariable("candidate-id") Long candidateId) throws CourtSearchNotFoundException {
        return new ResponseEntity<>(courtSearchService.getCourtSearchDetailsByCandidateId(candidateId),HttpStatus.OK);
    }
}
