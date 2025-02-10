package com.example.checkrr.controller;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.service.AdverseActionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/adverse-actions/filter")
    public ResponseEntity<Page<AdverseActionResponseDTO>> getAdverseActions(@RequestParam(value = "name",required = false) String name, @RequestParam(value = "status", required = false) AdverseActionStatus status, Pageable pageable){
        Page<AdverseActionResponseDTO> pageOfAdverseActionResponseDTOs=adverseActionService.getAdverseActions(name,status,pageable);
        return new ResponseEntity<>(pageOfAdverseActionResponseDTOs,HttpStatus.OK);
    }


}
