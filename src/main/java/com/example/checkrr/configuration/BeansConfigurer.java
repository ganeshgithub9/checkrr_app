package com.example.checkrr.configuration;

import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.entity.Candidate;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeansConfigurer {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper=new ModelMapper();
        TypeMap<CandidateDTO,Candidate> createCandidateRequestDTOToCandidateTypeMap=mapper.createTypeMap(CandidateDTO.class,Candidate.class);
        createCandidateRequestDTOToCandidateTypeMap.addMappings(map->{map.skip(Candidate::setId);map.skip(Candidate::setCreatedAt);});
        return mapper;
    }

}