package com.pusri.risk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pengendalian_risiko")
public class PengendalianRisiko {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String deskripsiRealisasi;
    
    private Double biaya;
    private String fileBuktiPath;
    private String periodeTriwulan;
    private int tahun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private RiskProject riskProject;

    public PengendalianRisiko() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeskripsiRealisasi() {
        return deskripsiRealisasi;
    }

    public void setDeskripsiRealisasi(String deskripsiRealisasi) {
        this.deskripsiRealisasi = deskripsiRealisasi;
    }

    public Double getBiaya() {
        return biaya;
    }

    public void setBiaya(Double biaya) {
        this.biaya = biaya;
    }

    public String getFileBuktiPath() {
        return fileBuktiPath;
    }

    public void setFileBuktiPath(String fileBuktiPath) {
        this.fileBuktiPath = fileBuktiPath;
    }

    public String getPeriodeTriwulan() {
        return periodeTriwulan;
    }

    public void setPeriodeTriwulan(String periodeTriwulan) {
        this.periodeTriwulan = periodeTriwulan;
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public RiskProject getRiskProject() {
        return riskProject;
    }

    public void setRiskProject(RiskProject riskProject) {
        this.riskProject = riskProject;
    }
}
