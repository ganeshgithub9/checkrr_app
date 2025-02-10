package com.example.checkrr.controller;

import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportController {

    ReportService reportService;

    ReportController(@Autowired ReportService reportService){
        this.reportService=reportService;
    }

    @GetMapping("/api/v1/candidates-with-reports/filter")
    public ResponseEntity<Page<CandidateWithReportDTO>> getCandidatesWithReports(@RequestParam(value = "name",required = false) String name, @RequestParam(value = "adjudication",required = false)Adjudication adjudication, @RequestParam(value = "adjudicationStatus",required = false) Status status, Pageable pageable){
        return new ResponseEntity<>(reportService.getCandidatesWithReports(name,adjudication,status,pageable), HttpStatus.OK);
    }

    @PostMapping("/api/v1/candidates-with-reports:export")
    public ResponseEntity<List<CandidateWithReportDTO>> getCandidatesWithReportsByDateRange(@RequestBody ExportDTO exportDTO){
        return new ResponseEntity<>(reportService.getCandidatesWithReportsByDateRange(exportDTO), HttpStatus.OK);
    }

}
