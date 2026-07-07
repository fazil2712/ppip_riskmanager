package com.pusri.risk.service;

import com.pusri.risk.model.RiskProject;
import com.pusri.risk.repository.RiskProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskProjectService {

    @Autowired
    private RiskProjectRepository riskProjectRepository;

    public List<RiskProject> getAllProjects() {
        return riskProjectRepository.findAll();
    }

    public void inputProject(RiskProject project) {
        project.calculateRiskMatrix();
        riskProjectRepository.save(project);
    }
    
    public void scoreRiskMatrix(Long projectId, int peluang, int dampak) {
        RiskProject project = riskProjectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.setPeluangScore(peluang);
            project.setDampakScore(dampak);
            project.calculateRiskMatrix();
            riskProjectRepository.save(project);
        }
    }
}
