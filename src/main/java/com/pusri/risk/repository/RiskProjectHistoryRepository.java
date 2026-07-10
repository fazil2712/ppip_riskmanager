package com.pusri.risk.repository;

import com.pusri.risk.model.RiskProjectHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskProjectHistoryRepository extends JpaRepository<RiskProjectHistory, Long> {
    List<RiskProjectHistory> findByProjectIdOrderByTimestampDesc(Long projectId);
    
    // Custom query to fetch histories based on user role and project owner
    List<RiskProjectHistory> findByDibuatOlehOrderByTimestampDesc(String dibuatOleh);
    List<RiskProjectHistory> findByRiskOwnerOrderByTimestampDesc(String riskOwner);

    @org.springframework.transaction.annotation.Transactional
    void deleteByProjectId(Long projectId);
}
