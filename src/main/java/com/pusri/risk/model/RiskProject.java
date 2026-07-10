package com.pusri.risk.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "risk_projects")
public class RiskProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // New attributes based on request
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
    
    // Year for filtering
    private Integer tahun;
    private String kuartal; // Q1, Q2, Q3, Q4
    
    // Risk Scoring
    private int peluangScore;
    private int dampakScore;
    private int totalRiskScore;
    
    private String riskLevel;
    private String status;

    // Approval Workflow Fields
    private String approvalStatus = "tersimpan"; // tersimpan, menunggu, open, rejected
    private String adminApproval = "pending";    // pending, approved, rejected
    private String riskOwnerApproval = "pending"; // pending, approved, rejected
    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    // Flag for Triwulan Updates
    private boolean updateTriwulanRequested = false;
    private Integer requestedTriwulanKe;
    private Integer requestedTriwulanTahun;

    @OneToMany(mappedBy = "riskProject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("triwulanKe ASC")
    private List<PengendalianRisiko> pengendalianRisikoList;
    
    @OneToMany(mappedBy = "riskProject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("rejectedAt DESC")
    private List<RejectionHistory> rejectionHistories;

    @Column(name = "notif_report_admin", columnDefinition = "boolean default false")
    private boolean notifReportAdmin = false;

    @Column(name = "notif_report_risk_owner", columnDefinition = "boolean default false")
    private boolean notifReportRiskOwner = false;

    @Column(name = "notif_identifikasi_risk_officer", columnDefinition = "boolean default false")
    private boolean notifIdentifikasiRiskOfficer = false;

    @Column(name = "notif_pengendalian_risk_officer", columnDefinition = "boolean default false")
    private boolean notifPengendalianRiskOfficer = false;

    public RiskProject() {}

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

        String[][] levelMatrix = {
            {"Low", "Low", "Low to Moderate", "Moderate", "High"}, // Peluang 1
            {"Low", "Low to Moderate", "Low to Moderate", "Moderate to High", "High"}, // Peluang 2
            {"Low", "Low to Moderate", "Moderate", "Moderate to High", "High"}, // Peluang 3
            {"Low", "Low to Moderate", "Moderate", "Moderate to High", "High"}, // Peluang 4
            {"Low to Moderate", "Moderate", "Moderate to High", "High", "High"} // Peluang 5
        };

        this.totalRiskScore = scoreMatrix[this.peluangScore - 1][this.dampakScore - 1];
        this.riskLevel = levelMatrix[this.peluangScore - 1][this.dampakScore - 1];
    }

    public String generateReport() {
        return "Report for Project: " + this.idRisiko + " | Level: " + this.riskLevel;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getIdRisiko() {
        return idRisiko;
    }

    public void setIdRisiko(String idRisiko) {
        this.idRisiko = idRisiko;
    }

    public String getDibuatOleh() {
        return dibuatOleh;
    }

    public void setDibuatOleh(String dibuatOleh) {
        this.dibuatOleh = dibuatOleh;
    }

    public String getRiskOwner() {
        return riskOwner;
    }

    public void setRiskOwner(String riskOwner) {
        this.riskOwner = riskOwner;
    }

    public String getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(String unitKerja) {
        this.unitKerja = unitKerja;
    }

    public String getSasaranUnitKerja() {
        return sasaranUnitKerja;
    }

    public void setSasaranUnitKerja(String sasaranUnitKerja) {
        this.sasaranUnitKerja = sasaranUnitKerja;
    }

    public String getSasaranUnitKerjaFile() {
        return sasaranUnitKerjaFile;
    }

    public void setSasaranUnitKerjaFile(String sasaranUnitKerjaFile) {
        this.sasaranUnitKerjaFile = sasaranUnitKerjaFile;
    }

    public String getKonteksEksternal() {
        return konteksEksternal;
    }

    public void setKonteksEksternal(String konteksEksternal) {
        this.konteksEksternal = konteksEksternal;
    }

    public String getKonteksEksternalFile() {
        return konteksEksternalFile;
    }

    public void setKonteksEksternalFile(String konteksEksternalFile) {
        this.konteksEksternalFile = konteksEksternalFile;
    }

    public String getKonteksInternal() {
        return konteksInternal;
    }

    public void setKonteksInternal(String konteksInternal) {
        this.konteksInternal = konteksInternal;
    }

    public String getKonteksInternalFile() {
        return konteksInternalFile;
    }

    public void setKonteksInternalFile(String konteksInternalFile) {
        this.konteksInternalFile = konteksInternalFile;
    }

    public String getRisiko() {
        return risiko;
    }

    public void setRisiko(String risiko) {
        this.risiko = risiko;
    }

    public String getRisikoFile() {
        return risikoFile;
    }

    public void setRisikoFile(String risikoFile) {
        this.risikoFile = risikoFile;
    }

    public String getKategoriRisiko() {
        return kategoriRisiko;
    }

    public void setKategoriRisiko(String kategoriRisiko) {
        this.kategoriRisiko = kategoriRisiko;
    }

    public String getPenyebab() {
        return penyebab;
    }

    public void setPenyebab(String penyebab) {
        this.penyebab = penyebab;
    }

    public String getPenyebabFile() {
        return penyebabFile;
    }

    public void setPenyebabFile(String penyebabFile) {
        this.penyebabFile = penyebabFile;
    }

    public String getDampak() {
        return dampak;
    }

    public void setDampak(String dampak) {
        this.dampak = dampak;
    }

    public String getDampakFile() {
        return dampakFile;
    }

    public void setDampakFile(String dampakFile) {
        this.dampakFile = dampakFile;
    }

    public String getExistingControl() {
        return existingControl;
    }

    public void setExistingControl(String existingControl) {
        this.existingControl = existingControl;
    }

    public String getExistingControlFile() {
        return existingControlFile;
    }

    public void setExistingControlFile(String existingControlFile) {
        this.existingControlFile = existingControlFile;
    }

    public String getRencanaPengendalian() {
        return rencanaPengendalian;
    }

    public void setRencanaPengendalian(String rencanaPengendalian) {
        this.rencanaPengendalian = rencanaPengendalian;
    }

    public String getRencanaPengendalianFile() {
        return rencanaPengendalianFile;
    }

    public void setRencanaPengendalianFile(String rencanaPengendalianFile) {
        this.rencanaPengendalianFile = rencanaPengendalianFile;
    }

    public Integer getTahun() {
        return tahun;
    }

    public void setTahun(Integer tahun) {
        this.tahun = tahun;
    }

    public String getKuartal() {
        return kuartal;
    }

    public void setKuartal(String kuartal) {
        this.kuartal = kuartal;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates and sets the totalRiskScore (1-25) and riskLevel
     * based on the custom 5x5 heatmap matrix.
     */
    public void calculateRiskLevels() {
        if (peluangScore < 1 || peluangScore > 5 || dampakScore < 1 || dampakScore > 5) {
            this.totalRiskScore = 0;
            this.riskLevel = "UNKNOWN";
            return;
        }
        
        // Matrix for scores 1-25. 
        // Row index: 5 - peluangScore (so P=5 is index 0, P=1 is index 4)
        // Col index: dampakScore - 1 (so D=1 is index 0, D=5 is index 4)
        int[][] scoreMatrix = {
            {7, 12, 17, 22, 25}, // P=5
            {4,  9, 14, 19, 24}, // P=4
            {3,  8, 13, 18, 23}, // P=3
            {2,  6, 11, 16, 21}, // P=2
            {1,  5, 10, 15, 20}  // P=1
        };
        
        String[][] levelMatrix = {
            {"Low to Moderate", "Moderate", "Moderate to High", "High", "High"}, // P=5
            {"Low", "Low to Moderate", "Moderate", "Moderate to High", "High"},  // P=4
            {"Low", "Low to Moderate", "Moderate", "Moderate to High", "High"},  // P=3
            {"Low", "Low to Moderate", "Low to Moderate", "Moderate to High", "High"}, // P=2
            {"Low", "Low", "Low to Moderate", "Moderate", "High"}                // P=1
        };
        
        int rowIndex = 5 - peluangScore;
        int colIndex = dampakScore - 1;
        
        this.totalRiskScore = scoreMatrix[rowIndex][colIndex];
        this.riskLevel = levelMatrix[rowIndex][colIndex];
    }

    public List<PengendalianRisiko> getPengendalianRisikoList() {
        return pengendalianRisikoList;
    }

    public void setPengendalianRisikoList(List<PengendalianRisiko> pengendalianRisikoList) {
        this.pengendalianRisikoList = pengendalianRisikoList;
    }

    public boolean isUpdateTriwulanRequested() {
        return updateTriwulanRequested;
    }

    public void setUpdateTriwulanRequested(boolean updateTriwulanRequested) {
        this.updateTriwulanRequested = updateTriwulanRequested;
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

    public List<RejectionHistory> getRejectionHistories() {
        return rejectionHistories;
    }

    public void setRejectionHistories(List<RejectionHistory> rejectionHistories) {
        this.rejectionHistories = rejectionHistories;
    }
    public Integer getRequestedTriwulanKe() {
        return requestedTriwulanKe;
    }

    public void setRequestedTriwulanKe(Integer requestedTriwulanKe) {
        this.requestedTriwulanKe = requestedTriwulanKe;
    }

    public Integer getRequestedTriwulanTahun() {
        return requestedTriwulanTahun;
    }

    public void setRequestedTriwulanTahun(Integer requestedTriwulanTahun) {
        this.requestedTriwulanTahun = requestedTriwulanTahun;
    }

    public boolean isNotifReportAdmin() {
        return notifReportAdmin;
    }

    public void setNotifReportAdmin(boolean notifReportAdmin) {
        this.notifReportAdmin = notifReportAdmin;
    }

    public boolean isNotifReportRiskOwner() {
        return notifReportRiskOwner;
    }

    public void setNotifReportRiskOwner(boolean notifReportRiskOwner) {
        this.notifReportRiskOwner = notifReportRiskOwner;
    }

    public boolean isNotifIdentifikasiRiskOfficer() {
        return notifIdentifikasiRiskOfficer;
    }

    public void setNotifIdentifikasiRiskOfficer(boolean notifIdentifikasiRiskOfficer) {
        this.notifIdentifikasiRiskOfficer = notifIdentifikasiRiskOfficer;
    }

    public boolean isNotifPengendalianRiskOfficer() {
        return notifPengendalianRiskOfficer;
    }

    public void setNotifPengendalianRiskOfficer(boolean notifPengendalianRiskOfficer) {
        this.notifPengendalianRiskOfficer = notifPengendalianRiskOfficer;
    }
}
