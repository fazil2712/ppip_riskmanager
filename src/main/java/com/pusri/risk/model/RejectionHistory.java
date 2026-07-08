package com.pusri.risk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rejection_histories")
public class RejectionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_project_id", nullable = false)
    private RiskProject riskProject;

    private String rejectionType; // "Identifikasi Risiko" or "Update Triwulan X"
    private String rejectedByRole; // "Admin" or "RiskOwner"
    private String rejectedByName;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDateTime rejectedAt;

    public RejectionHistory() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RiskProject getRiskProject() { return riskProject; }
    public void setRiskProject(RiskProject riskProject) { this.riskProject = riskProject; }

    public String getRejectionType() { return rejectionType; }
    public void setRejectionType(String rejectionType) { this.rejectionType = rejectionType; }

    public String getRejectedByRole() { return rejectedByRole; }
    public void setRejectedByRole(String rejectedByRole) { this.rejectedByRole = rejectedByRole; }

    public String getRejectedByName() { return rejectedByName; }
    public void setRejectedByName(String rejectedByName) { this.rejectedByName = rejectedByName; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }
}
