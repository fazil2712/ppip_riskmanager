package com.pusri.risk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String actor; // format: Name (Badge ID)
    private String actionType; // e.g., DELETE_PROJECT, APPROVE_TRIWULAN, REQUEST_UPDATE
    
    @Column(columnDefinition = "TEXT")
    private String description;

    public SystemLog() {
        this.timestamp = LocalDateTime.now();
    }

    public SystemLog(String actor, String actionType, String description) {
        this.timestamp = LocalDateTime.now();
        this.actor = actor;
        this.actionType = actionType;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
