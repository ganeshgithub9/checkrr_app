package com.example.checkrr.controller;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.dto.EmailMetaData;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.FileUploadFailedException;
import com.example.checkrr.exceptions.UserNotFoundException;
import com.example.checkrr.service.AdverseActionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

@RestController
public class AdverseActionController {

    AdverseActionService adverseActionService;
    ObjectMapper objectMapper;

    AdverseActionController(@Autowired AdverseActionService adverseActionService, @Autowired ObjectMapper objectMapper){
        this.adverseActionService=adverseActionService;
        this.objectMapper=objectMapper;
    }

    @PostMapping("/api/v1/candidates/{candidate-id}/adverse-actions")
    public ResponseEntity<String> createAdverseAction(@PathVariable("candidate-id") Long candidateId, @Valid @RequestBody AdverseActionDTO adverseActionDTO) throws CandidateNotFoundException, SQLException {
        return new ResponseEntity<>(adverseActionService.createAdverseAction(candidateId,adverseActionDTO), HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/v2/candidates/{candidate-id}/adverse-actions")
    public ResponseEntity<String> createAdverseActionWithMailAndAttachments(@PathVariable("candidate-id") Long candidateId, @RequestPart("adjudicationData") String stringifiedAdverseActionDTO, @RequestPart("emailMetaData") String stringifiedEmailMetaData, @RequestPart(value = "files",required = false) MultipartFile[] files) throws CandidateNotFoundException, UserNotFoundException, FileUploadFailedException, JsonProcessingException {
        AdverseActionDTO adverseActionDTO=objectMapper.readValue(stringifiedAdverseActionDTO, AdverseActionDTO.class);
        EmailMetaData emailMetaData=objectMapper.readValue(stringifiedEmailMetaData, EmailMetaData.class);
        return new ResponseEntity<>(adverseActionService.createAdverseActionWithMailAndAttachments(candidateId,adverseActionDTO,emailMetaData,files), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/adverse-actions/filter")
    public ResponseEntity<Page<AdverseActionResponseDTO>> getAdverseActions(@RequestParam(value = "name",required = false) String name, @RequestParam(value = "status", required = false) AdverseActionStatus status, Pageable pageable){
        Page<AdverseActionResponseDTO> pageOfAdverseActionResponseDTOs=adverseActionService.getAdverseActions(name,status,pageable);
        return new ResponseEntity<>(pageOfAdverseActionResponseDTOs,HttpStatus.OK);
    }


}
