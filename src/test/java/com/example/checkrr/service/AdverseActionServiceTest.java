package com.example.checkrr.service;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.dto.EmailMetaData;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.User;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.enums.Status;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.FileUploadFailedException;
import com.example.checkrr.exceptions.UserNotFoundException;
import com.example.checkrr.repository.AdverseActionRepository;
import com.example.checkrr.util.AttachmentUtil;
import com.example.checkrr.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.mail.MessagingException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    CustomUserService userService;

    @Mock
    AttachmentUtil attachmentUtil;

    @Mock
    AdverseActionRepository adverseActionRepository;

    @Mock
    EmailUtil emailUtil;


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

    User user;

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

        user=new User(3L,"jack","xyz@abc.com","@#45~@qa",null);

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

    @Test
    void givenCandidateIdAndAdverseActionDTOAndEmailMetaDataAndMultipartFiles_WhenCreateAdverseActionWithMailAndAttachments_ThenReturnsSuccessResponse() throws CandidateNotFoundException, FileUploadFailedException, UserNotFoundException, MessagingException {

        List<String> urlsList=List.of("/home/ganeb/uploads/checkrr/2__test1.txt","/home/ganeb/uploads/checkrr/2__test2.txt");
        MultipartFile file1 = new MockMultipartFile("files", "test1.txt", "text/plain", "Dummy file content 1".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("files", "test2.txt", "text/plain", "Dummy file content 2".getBytes(StandardCharsets.UTF_8));
        MultipartFile[] files=new MultipartFile[]{file1,file2};

        EmailMetaData emailMetaData=new EmailMetaData(3L,2L,"Pre-adverse action notice","<!DOCTYPE html><html lang=\\\"en\\\"><head><meta charset=\\\"UTF-8\\\"><meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\"><title>Pre Adverse Action Notice</title><style>body{font-family:Arial,sans-serif;margin:40px;line-height:1.6;}.container{max-width:600px;padding:20px;border:1px solid #ccc;border-radius:8px;}.checkbox-group{margin:20px 0;}</style></head><body><div class=\\\"container\\\"><p>Dear Jay,</p><p>You recently authorized checkr-bpo (\\\"the company\\\") to obtain consumer reports and/or investigate consumer reports about you from a consumer reporting agency. The Company is considering taking action in whole or in part based on information in such report(s) including the following specific items identified in the report prepared by Checkr, Inc.</p><h3>Select The Charges For The Pre Adverse Action</h3><div class=\\\"checkbox-group\\\"><input type=\\\"checkbox\\\" id=\\\"charge1\\\"><label for=\\\"charge1\\\">Driving while license suspended</label><br><input type=\\\"checkbox\\\" id=\\\"charge2\\\"><label for=\\\"charge2\\\">Assault Domestic Violence</label><br><input type=\\\"checkbox\\\" id=\\\"charge3\\\"><label for=\\\"charge3\\\">Unable to verify employment history at Dunder Mifflin</label></div>");


        when(candidateService.getReportIdByCandidateId(anyLong())).thenReturn(2L);
        when(candidateService.getReferenceByCandidateId(anyLong())).thenReturn(candidate);
        when(userService.getUserReferenceById(anyLong())).thenReturn(user);
        when(attachmentUtil.storeAttachmentAndGetURLs(any(MultipartFile[].class),anyLong())).thenReturn(urlsList);
        when(adverseActionRepository.save(any(AdverseAction.class))).thenReturn(adverseAction);
        when(reportService.updateAdjudicationDetails(any(AdjudicationUpdationDTO.class))).thenReturn(1);
        doNothing().when(emailUtil).sendMailWithAttachments(anyString(),anyString(),anyString(),anyString(),any(MultipartFile[].class));

        Long actualResult= adverseActionService.createAdverseActionWithMailAndAttachments(3L,adverseActionDTO,emailMetaData,files);

        verify(adverseActionRepository,times(1)).save(any(AdverseAction.class));
        verify(reportService, Mockito.times(1)).updateAdjudicationDetails(any(AdjudicationUpdationDTO.class));
        assertEquals(5,actualResult);
    }


    @Test
    void givenCandidateIdAndAdverseActionDTOAndEmailMetaDataAndMultipartFiles_WhenCreateAdverseActionWithMailAndAttachments_ThenThrowsUserNotFoundException() throws CandidateNotFoundException {

        MultipartFile file1 = new MockMultipartFile("files", "test1.txt", "text/plain", "Dummy file content 1".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("files", "test2.txt", "text/plain", "Dummy file content 2".getBytes(StandardCharsets.UTF_8));
        MultipartFile[] files=new MultipartFile[]{file1,file2};

        EmailMetaData emailMetaData=new EmailMetaData(3L,2L,"Pre-adverse action notice","<!DOCTYPE html><html lang=\\\"en\\\"><head><meta charset=\\\"UTF-8\\\"><meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\"><title>Pre Adverse Action Notice</title><style>body{font-family:Arial,sans-serif;margin:40px;line-height:1.6;}.container{max-width:600px;padding:20px;border:1px solid #ccc;border-radius:8px;}.checkbox-group{margin:20px 0;}</style></head><body><div class=\\\"container\\\"><p>Dear Jay,</p><p>You recently authorized checkr-bpo (\\\"the company\\\") to obtain consumer reports and/or investigate consumer reports about you from a consumer reporting agency. The Company is considering taking action in whole or in part based on information in such report(s) including the following specific items identified in the report prepared by Checkr, Inc.</p><h3>Select The Charges For The Pre Adverse Action</h3><div class=\\\"checkbox-group\\\"><input type=\\\"checkbox\\\" id=\\\"charge1\\\"><label for=\\\"charge1\\\">Driving while license suspended</label><br><input type=\\\"checkbox\\\" id=\\\"charge2\\\"><label for=\\\"charge2\\\">Assault Domestic Violence</label><br><input type=\\\"checkbox\\\" id=\\\"charge3\\\"><label for=\\\"charge3\\\">Unable to verify employment history at Dunder Mifflin</label></div>");


        when(candidateService.getReportIdByCandidateId(anyLong())).thenReturn(2L);
        when(candidateService.getReferenceByCandidateId(anyLong())).thenReturn(candidate);
        when(userService.getUserReferenceById(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class,()->adverseActionService.createAdverseActionWithMailAndAttachments(3L,adverseActionDTO,emailMetaData,files));

    }

}
