package com.example.checkrr.repository;

import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    @Modifying
    @Query("update Report r set r.adjudication= :#{#dto.adjudication}, r.adjudicationStatus=:#{#dto.status},r.adjudicationCreatedAt= :#{#dto.createdAt}, r.adjudicationCompletedAt=:#{#dto.completedAt} where r.id=:#{#dto.reportId}")
    int updateAdjudicationDetails(@Param("dto") AdjudicationUpdationDTO adjudicationUpdationDTO);
}