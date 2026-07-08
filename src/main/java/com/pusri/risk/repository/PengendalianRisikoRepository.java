package com.pusri.risk.repository;

import com.pusri.risk.model.PengendalianRisiko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PengendalianRisikoRepository extends JpaRepository<PengendalianRisiko, Long> {
    List<PengendalianRisiko> findByApprovalStatus(String approvalStatus);
    List<PengendalianRisiko> findByRiskProjectRiskOwnerAndApprovalStatus(String riskOwner, String approvalStatus);
    List<PengendalianRisiko> findByRiskProjectDibuatOlehAndApprovalStatusNot(String dibuatOleh, String approvalStatus);
    List<PengendalianRisiko> findByRiskProjectDibuatOlehAndApprovalStatus(String dibuatOleh, String approvalStatus);
}
