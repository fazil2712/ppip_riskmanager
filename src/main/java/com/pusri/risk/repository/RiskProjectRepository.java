package com.pusri.risk.repository;

import com.pusri.risk.model.RiskProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskProjectRepository extends JpaRepository<RiskProject, Long> {
    List<RiskProject> findByDibuatOleh(String dibuatOleh);
    List<RiskProject> findByUnitKerja(String unitKerja);
    List<RiskProject> findByRiskOwner(String riskOwner);
    
    boolean existsByIdRisiko(String idRisiko);
    
    List<RiskProject> findByTahun(Integer tahun);
    List<RiskProject> findByDibuatOlehAndTahun(String dibuatOleh, Integer tahun);
    List<RiskProject> findByUnitKerjaAndTahun(String unitKerja, Integer tahun);

    List<RiskProject> findByApprovalStatus(String approvalStatus);
    List<RiskProject> findByRiskOwnerAndApprovalStatus(String riskOwner, String approvalStatus);
    List<RiskProject> findByDibuatOlehAndApprovalStatusNot(String dibuatOleh, String approvalStatus);
    List<RiskProject> findByDibuatOlehAndApprovalStatus(String dibuatOleh, String approvalStatus);

    List<RiskProject> findByApprovalStatusIn(List<String> statuses);
    List<RiskProject> findByRiskOwnerAndApprovalStatusIn(String riskOwner, List<String> statuses);
    List<RiskProject> findByDibuatOlehAndApprovalStatusIn(String dibuatOleh, List<String> statuses);
    List<RiskProject> findByDibuatOlehAndUpdateTriwulanRequestedTrue(String dibuatOleh);

    boolean existsByNotifReportAdminTrue();
    boolean existsByNotifReportRiskOwnerTrueAndRiskOwner(String riskOwner);
    boolean existsByNotifIdentifikasiRiskOfficerTrueAndDibuatOleh(String dibuatOleh);
    boolean existsByNotifPengendalianRiskOfficerTrueAndDibuatOleh(String dibuatOleh);

    List<RiskProject> findByNotifReportAdminTrue();
    List<RiskProject> findByNotifReportRiskOwnerTrueAndRiskOwner(String riskOwner);
    List<RiskProject> findByNotifIdentifikasiRiskOfficerTrueAndDibuatOleh(String dibuatOleh);
    List<RiskProject> findByNotifPengendalianRiskOfficerTrueAndDibuatOleh(String dibuatOleh);
}
