package com.pusri.risk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pengendalian_risiko")
public class PengendalianRisiko {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String realisasiPengendalian;
    
    private int peluangScore;
    private int dampakScore;
    private int totalRiskScore;
    private String riskLevel;
    
    private int triwulanKe;
    private Integer triwulanTahun;

    // Approval Workflow Fields for Triwulan Update
    private String approvalStatus = "menunggu"; // menunggu, open, rejected
    private String adminApproval = "pending";    // pending, approved, rejected
    private String riskOwnerApproval = "pending"; // pending, approved, rejected
    
    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private RiskProject riskProject;

    public PengendalianRisiko() {}

    public void calculateRiskMatrix() {
        if (this.peluangScore < 1 || this.peluangScore > 5 || this.dampakScore < 1 || this.dampakScore > 5) {
            this.totalRiskScore = 0;
            this.riskLevel = "UNKNOWN";
            return;
        }

        int[][] scoreMatrix = {
            { 1,  5, 10, 15, 20}, // Peluang 1
            { 2,  6, 11, 16, 21}, // Peluang 2
            { 3,  8, 13, 18, 23}, // Peluang 3
            { 4,  9, 14, 19, 24}, // Peluang 4
            { 7, 12, 17, 22, 25}  // Peluang 5
        };

        this.totalRiskScore = scoreMatrix[this.peluangScore - 1][this.dampakScore - 1];

        if (this.totalRiskScore >= 1 && this.totalRiskScore <= 2) {
            this.riskLevel = "Very Low";
        } else if (this.totalRiskScore >= 3 && this.totalRiskScore <= 5) {
            this.riskLevel = "Low";
        } else if (this.totalRiskScore >= 6 && this.totalRiskScore <= 11) {
            this.riskLevel = "Moderate";
        } else if (this.totalRiskScore >= 12 && this.totalRiskScore <= 19) {
            this.riskLevel = "Moderate to High";
        } else if (this.totalRiskScore >= 20 && this.totalRiskScore <= 25) {
            this.riskLevel = "High";
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealisasiPengendalian() {
        return realisasiPengendalian;
    }

    public void setRealisasiPengendalian(String realisasiPengendalian) {
        this.realisasiPengendalian = realisasiPengendalian;
    }

    public int getPeluangScore() {
        return peluangScore;
    }

    public void setPeluangScore(int peluangScore) {
        this.peluangScore = peluangScore;
    }

    public int getDampakScore() {
        return dampakScore;
    }

    public void setDampakScore(int dampakScore) {
        this.dampakScore = dampakScore;
    }

    public int getTotalRiskScore() {
        return totalRiskScore;
    }

    public void setTotalRiskScore(int totalRiskScore) {
        this.totalRiskScore = totalRiskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getTriwulanKe() {
        return triwulanKe;
    }

    public void setTriwulanKe(int triwulanKe) {
        this.triwulanKe = triwulanKe;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getAdminApproval() {
        return adminApproval;
    }

    public void setAdminApproval(String adminApproval) {
        this.adminApproval = adminApproval;
    }

    public String getRiskOwnerApproval() {
        return riskOwnerApproval;
    }

    public void setRiskOwnerApproval(String riskOwnerApproval) {
        this.riskOwnerApproval = riskOwnerApproval;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public RiskProject getRiskProject() {
        return riskProject;
    }

    public void setRiskProject(RiskProject riskProject) {
        this.riskProject = riskProject;
    }

    public Integer getTriwulanTahun() {
        return triwulanTahun;
    }

    public void setTriwulanTahun(Integer triwulanTahun) {
        this.triwulanTahun = triwulanTahun;
    }
}
