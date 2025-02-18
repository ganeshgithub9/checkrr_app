package com.example.checkrr.configuration;

import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeansConfigurer {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper=new ModelMapper();
        Condition<?, ?> skipNull = ctx -> ctx.getSource() != null;
        mapper.getConfiguration().setPropertyCondition(skipNull);
        TypeMap<CandidateDTO,Candidate> createCandidateRequestDTOToCandidateTypeMap=mapper.createTypeMap(CandidateDTO.class,Candidate.class);
        createCandidateRequestDTOToCandidateTypeMap.addMappings(map->{map.skip(Candidate::setId);map.skip(Candidate::setCreatedAt);});

        TypeMap<AdverseAction, AdverseActionResponseDTO> createAdverseActionToAdverseActionResponseDTOTypeMap=mapper.createTypeMap(AdverseAction.class, AdverseActionResponseDTO.class);
        createAdverseActionToAdverseActionResponseDTOTypeMap.addMappings(mapping->mapping.map(adverseAction -> adverseAction.getCandidate().getName(),AdverseActionResponseDTO::setName));

        TypeMap<Report, CandidateWithReportDTO> reportToCandidateWithReportDTOTypeMap=mapper.createTypeMap(Report.class, CandidateWithReportDTO.class);
        reportToCandidateWithReportDTOTypeMap.addMappings(mapping->{
            mapping.map(report -> report.getCandidate().getName(),CandidateWithReportDTO::setName);
            mapping.map(report -> report.getCandidate().getId(),CandidateWithReportDTO::setCandidateId);
            mapping.map(report -> report.getCandidate().getLocation(),CandidateWithReportDTO::setLocation);
        });
        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}