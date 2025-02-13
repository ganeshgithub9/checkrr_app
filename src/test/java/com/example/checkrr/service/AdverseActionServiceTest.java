package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.enums.Status;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.repository.AdverseActionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    AdjudicationUpdationDTO adjudicationUpdationDTO;

    @BeforeEach
    void setup(){
        openMocks(this);

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


        adjudicationUpdationDTO=new AdjudicationUpdationDTO();
        adjudicationUpdationDTO.setReportId(3L);
        adjudicationUpdationDTO.setCompletedAt(LocalDate.now());
        adjudicationUpdationDTO.setStatus(Status.CONSIDER);
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


}
