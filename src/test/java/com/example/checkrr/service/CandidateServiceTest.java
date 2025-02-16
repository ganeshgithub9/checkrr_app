package com.example.checkrr.service;

import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.dto.ReportDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.ReportNotFoundException;
import com.example.checkrr.projection.CandidateGeneralInfoProjection;
import com.example.checkrr.repository.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class CandidateServiceTest {

    @Mock
    CandidateRepository candidateRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CustomCandidateService candidateService;

    Candidate candidate;
    CandidateDTO candidateDTO;
    CandidateGeneralInfoProjection candidateGeneralInfoProjection;
    ReportDTO reportDTO;
    @BeforeEach
    void setup(){
        openMocks(this);
        candidate=new Candidate();
        candidate.setId(2L);
        candidate.setEmail("abb@abc.us");
        candidate.setName("man");
        candidate.setLocation("delhi");

        candidateDTO=new CandidateDTO();
        candidateDTO.setId(3L);
        candidateDTO.setEmail("abb@abc.us");
        candidateDTO.setName("man");
        candidateDTO.setLocation("delhi");

        candidateGeneralInfoProjection=new CandidateGeneralInfoProjection() {
            @Override
            public Long getId() {
                return 6L;
            }

            @Override
            public String getName() {
                return "man";
            }

            @Override
            public String getDriversLicenseNumber() {
                return "ASS21";
            }

            @Override
            public String getLocation() {
                return "Rome";
            }

            @Override
            public String getEmail() {
                return "tras@x.en";
            }

            @Override
            public String getDob() {
                return "";
            }

            @Override
            public String getPhone() {
                return "";
            }

            @Override
            public Integer getZipcode() {
                return 0;
            }

            @Override
            public String getSocialSecurityNumber() {
                return "";
            }

            @Override
            public LocalDate getCreatedAt() {
                return null;
            }
        };

        reportDTO=new ReportDTO();
        reportDTO.setId(5L);
        reportDTO.setAdjudicationCreatedAt(LocalDate.now());
        reportDTO.setAdjudication(Adjudication.ENGAGE);
    }


    @Test
    void givenCandidateDetails_WhenCreateCandidate_ThenReturnsCandidate() throws SQLException {

        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);
        when(modelMapper.map(candidateDTO, Candidate.class)).thenReturn(candidate);
        String actualResult=candidateService.createCandidate(candidateDTO);
        assertEquals("Created a candidate with id 2",actualResult);
    }

    @Test
    void givenCandidateId_WhenGetCandidateGeneralInfoById_ThenReturnsCandidateDTO() throws CandidateNotFoundException {

        when(candidateRepository.findCandidateGeneralInfoById(anyLong())).thenReturn(Optional.of(candidateGeneralInfoProjection));
        when(modelMapper.map(candidateGeneralInfoProjection, CandidateDTO.class)).thenReturn(candidateDTO);

        CandidateDTO actualResult=candidateService.getCandidateGeneralInfoById(3L);
        assertNotNull(actualResult);
        assert(actualResult.equals(candidateDTO));
    }

    @Test
    void givenInvalidCandidateId_WhenGetCandidateGeneralInfoById_ThenThrowsCandidateNotFoundException()  {

        when(candidateRepository.findCandidateGeneralInfoById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class,()->candidateService.getCandidateGeneralInfoById(3L));

    }

    @Test
    void givenCandidateId_WhenGetReportByCandidateId_ThenReturnsReportDTO() throws ReportNotFoundException {

        when(candidateRepository.findReportByCandidateId(anyLong())).thenReturn(Optional.of(reportDTO));

        ReportDTO actualResult=candidateService.getReportByCandidateId(3L);
        assertNotNull(actualResult);
        assert(actualResult.equals(reportDTO));
    }

    @Test
    void givenInvalidCandidateId_WhenGetReportByCandidateId_ThenThrowsReportNotFoundException()  {

        when(candidateRepository.findReportByCandidateId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReportNotFoundException.class,()->candidateService.getReportByCandidateId(3L));
    }

    @Test
    void givenCandidateId_WhenGetReferenceByCandidateId_ThenReturnsCandidate() {

        when(candidateRepository.getReferenceById(anyLong())).thenReturn(candidate);

        Candidate actualResult=candidateService.getReferenceByCandidateId(3L);
        assertNotNull(actualResult);
        assert(actualResult.equals(candidate));
    }



    @Test
    void givenCandidateId_WhenGetReportIdByCandidateId_ThenReturnsReportId() throws CandidateNotFoundException {

        when(candidateRepository.findReportIdById(anyLong())).thenReturn(Optional.of(3L));

        Long actualResult=candidateService.getReportIdByCandidateId(3L);
        assertNotNull(actualResult);
        assertEquals(3L,actualResult);
        assert(actualResult.equals(3L));
    }

    @Test
    void givenInvalidCandidateId_WhenGetReportIdByCandidateId_ThenThrowsReportNotFoundException()  {

        when(candidateRepository.findReportIdById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class,()->candidateService.getReportIdByCandidateId(3L));
    }

    @Test
    void givenCandidateDTO_WhenToCandidate_ThenReturnsCandidate() {

        when(modelMapper.map(candidateDTO, Candidate.class)).thenReturn(candidate);

        Candidate actualResult=candidateService.toCandidate(candidateDTO);
        assertNotNull(actualResult);
        assertEquals(actualResult,candidate);
        assert(actualResult.getName().equals(candidate.getName()));
    }

    @Test
    void givenCandidate_WhenToCandidateDTO_ThenReturnsCandidateDTO() {

        when(modelMapper.map(candidate, CandidateDTO.class)).thenReturn(candidateDTO);

        CandidateDTO actualResult=candidateService.toCandidateDTO(candidate);
        assertNotNull(actualResult);
        assert(actualResult.equals(candidateDTO));
        assert(actualResult.getName().equals(candidate.getName()));
    }

}
