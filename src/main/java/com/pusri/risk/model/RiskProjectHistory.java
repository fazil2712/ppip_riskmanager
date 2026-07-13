package com.pusri.risk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_project_histories")
public class RiskProjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The ID of the original project
    private Long projectId;
    
    private String actionType; // e.g., INITIAL_SUBMIT, UPDATE_TRIWULAN_1
    private LocalDateTime timestamp;
    
    // Project data snapshot
    private String idRisiko;
    private String dibuatOleh;
    private String unitKerja;
    private String riskOwner;
    
    @Column(columnDefinition = "TEXT")
    private String sasaranUnitKerja;
    private String sasaranUnitKerjaFile;
    
    @Column(columnDefinition = "TEXT")
    private String konteksEksternal;
    private String konteksEksternalFile;
    
    @Column(columnDefinition = "TEXT")
    private String konteksInternal;
    private String konteksInternalFile;
    
    @Column(columnDefinition = "TEXT")
    private String risiko;
    private String risikoFile;
    
    private String kategoriRisiko;
    
    @Column(columnDefinition = "TEXT")
    private String penyebab;
    private String penyebabFile;
    
    @Column(columnDefinition = "TEXT")
    private String dampak;
    private String dampakFile;
    
    @Column(columnDefinition = "TEXT")
    private String existingControl;
    private String existingControlFile;
    
    @Column(columnDefinition = "TEXT")
    private String rencanaPengendalian;
    private String rencanaPengendalianFile;
    
    @Column(columnDefinition = "TEXT")
    private String realisasiPengendalian; // Only populated for triwulan updates
    private String realisasiPengendalianFile;
    
    // Risk Scoring Snapshot
    private int peluangScore;
    private int dampakScore;
    private int totalRiskScore;
    private String riskLevel;
    
    // Approval Status for this specific submission
    private String approvalStatus = "menunggu"; // menunggu, open, rejected
    private String adminApproval = "pending";
    private String riskOwnerApproval = "pending";
    
    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private Integer triwulanTahun;

    public RiskProjectHistory() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor to clone from RiskProject (for Identifikasi)
    public RiskProjectHistory(RiskProject project, String actionType) {
        this.projectId = project.getId();
        this.actionType = actionType;
        this.timestamp = LocalDateTime.now();
        
        this.idRisiko = project.getIdRisiko();
        this.dibuatOleh = project.getDibuatOleh();
        this.unitKerja = project.getUnitKerja();
        this.riskOwner = project.getRiskOwner();
        this.sasaranUnitKerja = project.getSasaranUnitKerja();
        this.konteksEksternal = project.getKonteksEksternal();
        this.konteksInternal = project.getKonteksInternal();
        this.risiko = project.getRisiko();
        this.kategoriRisiko = project.getKategoriRisiko();
        this.penyebab = project.getPenyebab();
        this.dampak = project.getDampak();
        this.existingControl = project.getExistingControl();
        this.rencanaPengendalian = project.getRencanaPengendalian();
        
        this.sasaranUnitKerjaFile = project.getSasaranUnitKerjaFile();
        this.konteksEksternalFile = project.getKonteksEksternalFile();
        this.konteksInternalFile = project.getKonteksInternalFile();
        this.risikoFile = project.getRisikoFile();
        this.penyebabFile = project.getPenyebabFile();
        this.dampakFile = project.getDampakFile();
        this.existingControlFile = project.getExistingControlFile();
        this.rencanaPengendalianFile = project.getRencanaPengendalianFile();
        
        this.peluangScore = project.getPeluangScore();
        this.dampakScore = project.getDampakScore();
        this.totalRiskScore = project.getTotalRiskScore();
        this.riskLevel = project.getRiskLevel();
        
        this.approvalStatus = project.getApprovalStatus();
        this.adminApproval = project.getAdminApproval();
        this.riskOwnerApproval = project.getRiskOwnerApproval();
        this.rejectionReason = project.getRejectionReason();
    }

    // Constructor to clone from PengendalianRisiko (for Triwulan)
    public RiskProjectHistory(RiskProject project, PengendalianRisiko triwulan, String actionType) {
        this(project, actionType); // Call primary constructor for base data
        
        // Override with triwulan specific data
        this.realisasiPengendalian = triwulan.getRealisasiPengendalian();
        this.realisasiPengendalianFile = triwulan.getRealisasiPengendalianFile();
        this.peluangScore = triwulan.getPeluangScore();
        this.dampakScore = triwulan.getDampakScore();
        this.totalRiskScore = triwulan.getTotalRiskScore();
        this.riskLevel = triwulan.getRiskLevel();
        
        this.approvalStatus = triwulan.getApprovalStatus();
        this.adminApproval = triwulan.getAdminApproval();
        this.riskOwnerApproval = triwulan.getRiskOwnerApproval();
        this.rejectionReason = triwulan.getRejectionReason();
        this.triwulanTahun = triwulan.getTriwulanTahun();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    

    public String getIdRisiko() { return idRisiko; }
    public void setIdRisiko(String idRisiko) { this.idRisiko = idRisiko; }
    public String getDibuatOleh() { return dibuatOleh; }
    public void setDibuatOleh(String dibuatOleh) { this.dibuatOleh = dibuatOleh; }
    public String getUnitKerja() { return unitKerja; }
    public void setUnitKerja(String unitKerja) { this.unitKerja = unitKerja; }
    public String getRiskOwner() { return riskOwner; }
    public void setRiskOwner(String riskOwner) { this.riskOwner = riskOwner; }
    public String getSasaranUnitKerja() { return sasaranUnitKerja; }
    public void setSasaranUnitKerja(String sasaranUnitKerja) { this.sasaranUnitKerja = sasaranUnitKerja; }
    public String getSasaranUnitKerjaFile() { return sasaranUnitKerjaFile; }
    public void setSasaranUnitKerjaFile(String sasaranUnitKerjaFile) { this.sasaranUnitKerjaFile = sasaranUnitKerjaFile; }
    public String getKonteksEksternal() { return konteksEksternal; }
    public void setKonteksEksternal(String konteksEksternal) { this.konteksEksternal = konteksEksternal; }
    public String getKonteksEksternalFile() { return konteksEksternalFile; }
    public void setKonteksEksternalFile(String konteksEksternalFile) { this.konteksEksternalFile = konteksEksternalFile; }
    public String getKonteksInternal() { return konteksInternal; }
    public void setKonteksInternal(String konteksInternal) { this.konteksInternal = konteksInternal; }
    public String getKonteksInternalFile() { return konteksInternalFile; }
    public void setKonteksInternalFile(String konteksInternalFile) { this.konteksInternalFile = konteksInternalFile; }
    public String getRisiko() { return risiko; }
    public void setRisiko(String risiko) { this.risiko = risiko; }
    public String getRisikoFile() { return risikoFile; }
    public void setRisikoFile(String risikoFile) { this.risikoFile = risikoFile; }
    public String getKategoriRisiko() { return kategoriRisiko; }
    public void setKategoriRisiko(String kategoriRisiko) { this.kategoriRisiko = kategoriRisiko; }
    public String getPenyebab() { return penyebab; }
    public void setPenyebab(String penyebab) { this.penyebab = penyebab; }
    public String getPenyebabFile() { return penyebabFile; }
    public void setPenyebabFile(String penyebabFile) { this.penyebabFile = penyebabFile; }
    public String getDampak() { return dampak; }
    public void setDampak(String dampak) { this.dampak = dampak; }
    public String getDampakFile() { return dampakFile; }
    public void setDampakFile(String dampakFile) { this.dampakFile = dampakFile; }
    public String getExistingControl() { return existingControl; }
    public void setExistingControl(String existingControl) { this.existingControl = existingControl; }
    public String getExistingControlFile() { return existingControlFile; }
    public void setExistingControlFile(String existingControlFile) { this.existingControlFile = existingControlFile; }
    public String getRencanaPengendalian() { return rencanaPengendalian; }
    public void setRencanaPengendalian(String rencanaPengendalian) { this.rencanaPengendalian = rencanaPengendalian; }
    public String getRencanaPengendalianFile() { return rencanaPengendalianFile; }
    public void setRencanaPengendalianFile(String rencanaPengendalianFile) { this.rencanaPengendalianFile = rencanaPengendalianFile; }
    public String getRealisasiPengendalian() { return realisasiPengendalian; }
    public void setRealisasiPengendalian(String realisasiPengendalian) { this.realisasiPengendalian = realisasiPengendalian; }
    public String getRealisasiPengendalianFile() { return realisasiPengendalianFile; }
    public void setRealisasiPengendalianFile(String realisasiPengendalianFile) { this.realisasiPengendalianFile = realisasiPengendalianFile; }
    
    public int getPeluangScore() { return peluangScore; }
    public void setPeluangScore(int peluangScore) { this.peluangScore = peluangScore; }
    public int getDampakScore() { return dampakScore; }
    public void setDampakScore(int dampakScore) { this.dampakScore = dampakScore; }
    public int getTotalRiskScore() { return totalRiskScore; }
    public void setTotalRiskScore(int totalRiskScore) { this.totalRiskScore = totalRiskScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getAdminApproval() { return adminApproval; }
    public void setAdminApproval(String adminApproval) { this.adminApproval = adminApproval; }
    public String getRiskOwnerApproval() { return riskOwnerApproval; }
    public void setRiskOwnerApproval(String riskOwnerApproval) { this.riskOwnerApproval = riskOwnerApproval; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public Integer getTriwulanTahun() { return triwulanTahun; }
    public void setTriwulanTahun(Integer triwulanTahun) { this.triwulanTahun = triwulanTahun; }
}
