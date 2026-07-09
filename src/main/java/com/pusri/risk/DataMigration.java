package com.pusri.risk;

import com.pusri.risk.model.PengendalianRisiko;
import com.pusri.risk.model.RiskProjectHistory;
import com.pusri.risk.repository.PengendalianRisikoRepository;
import com.pusri.risk.repository.RiskProjectHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Component
public class DataMigration implements CommandLineRunner {

    @Autowired
    private PengendalianRisikoRepository pengendalianRisikoRepository;

    @Autowired
    private RiskProjectHistoryRepository riskProjectHistoryRepository;

    @Autowired
    private com.pusri.risk.repository.RiskProjectRepository riskProjectRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Running Data Migration for older Triwulan records...");

        // Migrate PengendalianRisiko
        List<PengendalianRisiko> list = pengendalianRisikoRepository.findAll();
        boolean changedPR = false;
        for (PengendalianRisiko pr : list) {
            if (pr.getTriwulanTahun() == null && pr.getRiskProject() != null) {
                pr.setTriwulanTahun(pr.getRiskProject().getTahun());
                changedPR = true;
            }
        }
        if (changedPR) {
            pengendalianRisikoRepository.saveAll(list);
            System.out.println("Migrated PengendalianRisiko records.");
        }

        // Migrate RiskProjectHistory
        List<RiskProjectHistory> historyList = riskProjectHistoryRepository.findAll();
        boolean changedHistory = false;
        for (RiskProjectHistory h : historyList) {
            if (h.getTriwulanTahun() == null && h.getActionType() != null && h.getActionType().startsWith("UPDATE_TRIWULAN_")) {
                com.pusri.risk.model.RiskProject project = riskProjectRepository.findById(h.getProjectId()).orElse(null);
                if (project != null && project.getTahun() != null) {
                    h.setTriwulanTahun(project.getTahun());
                    changedHistory = true;
                }
            }
        }
        if (changedHistory) {
            riskProjectHistoryRepository.saveAll(historyList);
            System.out.println("Migrated RiskProjectHistory records.");
        }
        
        System.out.println("Data Migration completed.");
    }
}
