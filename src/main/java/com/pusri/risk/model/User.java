package com.pusri.risk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    private String badgeId;
    
    private String password;
    
    // For roles, we will use strings like "Admin", "RiskOfficer", "RiskOwner"
    private String role; 
    
    private String fotoProfil;
    private String departemen;

    private java.time.LocalDateTime identifikasiStartDate;
    private java.time.LocalDateTime identifikasiDeadline;
    
    @Column(columnDefinition = "TEXT")
    private String identifikasiCatatan;
    
    private java.time.LocalDateTime triwulanStartDate;
    private java.time.LocalDateTime triwulanDeadline;
    
    @Column(columnDefinition = "TEXT")
    private String triwulanCatatan;

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public java.time.LocalDateTime getIdentifikasiDeadline() {
        return identifikasiDeadline;
    }

    public void setIdentifikasiDeadline(java.time.LocalDateTime identifikasiDeadline) {
        this.identifikasiDeadline = identifikasiDeadline;
    }

    public String getIdentifikasiCatatan() {
        return identifikasiCatatan;
    }

    public void setIdentifikasiCatatan(String identifikasiCatatan) {
        this.identifikasiCatatan = identifikasiCatatan;
    }

    public java.time.LocalDateTime getTriwulanDeadline() {
        return triwulanDeadline;
    }

    public void setTriwulanDeadline(java.time.LocalDateTime triwulanDeadline) {
        this.triwulanDeadline = triwulanDeadline;
    }

    public String getTriwulanCatatan() {
        return triwulanCatatan;
    }

    public void setTriwulanCatatan(String triwulanCatatan) {
        this.triwulanCatatan = triwulanCatatan;
    }

    public java.time.LocalDateTime getIdentifikasiStartDate() {
        return identifikasiStartDate;
    }

    public void setIdentifikasiStartDate(java.time.LocalDateTime identifikasiStartDate) {
        this.identifikasiStartDate = identifikasiStartDate;
    }

    public java.time.LocalDateTime getTriwulanStartDate() {
        return triwulanStartDate;
    }

    public void setTriwulanStartDate(java.time.LocalDateTime triwulanStartDate) {
        this.triwulanStartDate = triwulanStartDate;
    }
}
