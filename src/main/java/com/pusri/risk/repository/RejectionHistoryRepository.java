package com.pusri.risk.repository;

import com.pusri.risk.model.RejectionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RejectionHistoryRepository extends JpaRepository<RejectionHistory, Long> {
    List<RejectionHistory> findByRiskProjectIdOrderByRejectedAtDesc(Long riskProjectId);

    @org.springframework.transaction.annotation.Transactional
    void deleteByRiskProjectId(Long riskProjectId);
}
