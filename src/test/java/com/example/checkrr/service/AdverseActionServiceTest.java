package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.enums.Status;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.repository.AdverseActionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class AdverseActionServiceTest {

    @Mock
    CustomCandidateService candidateService;

    @Mock
    CustomReportService reportService;

    @Mock
    AdverseActionRepository adverseActionRepository;



    @InjectMocks
    CustomAdverseActionService adverseActionService;

    AdverseActionDTO adverseActionDTO;
    Candidate candidate;
    AdverseAction adverseAction;

    Page<AdverseAction> pageOfAdverseActions;

    AdjudicationUpdationDTO adjudicationUpdationDTO;

    AdverseActionResponseDTO adverseActionResponseDTO;
    Pageable pageable;
    Page<AdverseActionResponseDTO> pageOfAdverseActionResponseDTOs;

    @Spy
    private ModelMapper modelMapper=new ModelMapper();

    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        openMocks(this);

        TypeMap<AdverseAction, AdverseActionResponseDTO> createAdverseActionToAdverseActionResponseDTOTypeMap=modelMapper.createTypeMap(AdverseAction.class, AdverseActionResponseDTO.class);
        createAdverseActionToAdverseActionResponseDTOTypeMap.addMappings(mapping->mapping.map(adverseAction1 -> adverseAction1.getCandidate().getName(),AdverseActionResponseDTO::setName));

        candidate=new Candidate();
        candidate.setId(2L);
        candidate.setEmail("abb@abc.us");
        candidate.setName("man");
        candidate.setLocation("delhi");

        adverseActionDTO=new AdverseActionDTO(Adjudication.PRE_ADVERSE_ACTION, Status.CONSIDER,6);

        adverseAction=new AdverseAction();
        adverseAction.setId(5L);
        adverseAction.setStatus(AdverseActionStatus.SCHEDULED);
        adverseAction.setPreNoticeDate(LocalDate.now());
        adverseAction.setPostNoticeDate(LocalDate.now().plusDays(4));
        adverseAction.setCandidate(candidate);


        adjudicationUpdationDTO=new AdjudicationUpdationDTO();
        adjudicationUpdationDTO.setReportId(3L);
        adjudicationUpdationDTO.setCompletedAt(LocalDate.now());
        adjudicationUpdationDTO.setStatus(Status.CONSIDER);

        AdverseAction adverseAction2=new AdverseAction(3L,LocalDate.now(),LocalDate.now().plusDays(3),candidate, AdverseActionStatus.SCHEDULED);

        pageable= PageRequest.of(0,5);
        pageOfAdverseActions =new PageImpl<>(List.of(adverseAction,adverseAction2),pageable,10);

        adverseActionResponseDTO=modelMapper.map(adverseAction, AdverseActionResponseDTO.class);
        AdverseActionResponseDTO adverseActionResponseDTO2=modelMapper.map(adverseAction2, AdverseActionResponseDTO.class);
        pageOfAdverseActionResponseDTOs=new PageImpl<>(List.of(adverseActionResponseDTO,adverseActionResponseDTO2),pageable,10);

        objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


    }

    @Test
    void givenCandidateIdAndAdverseActionDTO_WhenCreateAdverseAction_ThenReturnsSuccessResponse() throws SQLException, CandidateNotFoundException {

        when(candidateService.getReportIdByCandidateId(3L)).thenReturn(2L);
        when(candidateService.getReferenceByCandidateId(3L)).thenReturn(candidate);
        when(adverseActionRepository.save(any(AdverseAction.class))).thenReturn(adverseAction);
        when(reportService.updateAdjudicationDetails(any(AdjudicationUpdationDTO.class))).thenReturn(1);

        String actualResult= adverseActionService.createAdverseAction(3L,adverseActionDTO);

        verify(reportService, Mockito.times(1)).updateAdjudicationDetails(any(AdjudicationUpdationDTO.class));
        assertEquals("Adverse action created with id 5",actualResult);
    }

    @Test
    void givenReportIdAndAdverseActionDTO_WhenToAdjudicationUpdationDTO_ThenReturnsAdjudicationUpdationDTO(){

        AdjudicationUpdationDTO actualResult= adverseActionService.toAdjudicationUpdationDTO(3L,adverseActionDTO);
        assertEquals(adverseActionDTO.getAdjudication(),actualResult.getAdjudication());
        assertEquals(3L,actualResult.getReportId());
        assertEquals(adverseActionDTO.getStatus(),actualResult.getStatus());
    }


    @Test
    void given_Name_Status_PageableObject_WhenGetAdverseActions_ThenReturnsAPageOfAdverseActionResponseDTOs() throws JsonProcessingException {

        when(adverseActionRepository.findAll(any(Specification.class),  any(Pageable.class))).thenReturn(pageOfAdverseActions);

        Page<AdverseActionResponseDTO> actualPage= adverseActionService.getAdverseActions("ma",AdverseActionStatus.SCHEDULED,pageable);

        assertEquals(pageOfAdverseActionResponseDTOs.getPageable().getPageSize(),actualPage.getPageable().getPageSize());
        String actualResult=objectMapper.writeValueAsString(actualPage),expectedResult=objectMapper.writeValueAsString(pageOfAdverseActionResponseDTOs);
        assertEquals(expectedResult,actualResult);
        assert(pageOfAdverseActionResponseDTOs.getContent().get(0).getPostNoticeDate().equals(actualPage.getContent().get(0).getPostNoticeDate()));
        assertEquals(pageOfAdverseActionResponseDTOs.getContent().get(1).getId(),actualPage.getContent().get(1).getId());

    }


}
