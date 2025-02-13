package com.example.checkrr.repository;

import com.example.checkrr.dto.ReportDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.projection.CandidateGeneralInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {


     Optional<CandidateGeneralInfoProjection> findCandidateGeneralInfoById(Long candidateId);

     @Query("select new com.example.checkrr.dto.ReportDTO(r.id, r.adjudication,r.adjudicationCreatedAt,r.adjudicationCompletedAt,r.adjudicationStatus,r.packageType,r.turnAroundTime) from Candidate c join c.report r where c.id= :candidateId")
     Optional<ReportDTO> findReportByCandidateId(@Param("candidateId") Long candidateId);

     @Query(value = "select report_id from candidate where id= :candidateId",nativeQuery = true)
     Optional<Long> findReportIdById(Long candidateId);
}