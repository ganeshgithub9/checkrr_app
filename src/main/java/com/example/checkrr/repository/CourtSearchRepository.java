package com.example.checkrr.repository;

import com.example.checkrr.entity.CourtSearch;
import com.example.checkrr.projection.CourtSearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtSearchRepository extends JpaRepository<CourtSearch, Long> {


    Optional<List<CourtSearchProjection>> getCourtSearchDetailsByCandidateId(Long candidateId);
}