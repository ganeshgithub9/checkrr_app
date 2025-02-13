package com.example.checkrr.controller;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.service.AdverseActionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1")
public class AdverseActionController {

    AdverseActionService adverseActionService;

    AdverseActionController(@Autowired AdverseActionService adverseActionService){
        this.adverseActionService=adverseActionService;
    }

    @PostMapping("/candidates/{candidate-id}/adverse-actions")
    public ResponseEntity<String> createAdverseAction(@PathVariable("candidate-id") Long candidateId, @Valid @RequestBody AdverseActionDTO adverseActionDTO) throws CandidateNotFoundException, SQLException {
        return new ResponseEntity<>(adverseActionService.createAdverseAction(candidateId,adverseActionDTO), HttpStatus.CREATED);
    }


}
