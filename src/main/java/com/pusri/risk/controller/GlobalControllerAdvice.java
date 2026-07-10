package com.pusri.risk.controller;

import com.pusri.risk.model.User;
import com.pusri.risk.repository.RiskProjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private RiskProjectRepository riskProjectRepository;
    
    @Autowired
    private com.pusri.risk.repository.UserRepository userRepository;

    @ModelAttribute
    public void addNotificationAttributes(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser != null) {
            // Refresh user from DB to get latest deadlines
            User user = userRepository.findById(sessionUser.getId()).orElse(sessionUser);
            // Update session with fresh user
            session.setAttribute("loggedInUser", user);
            
            boolean hasReportNotif = false;
            boolean hasIdentifikasiNotif = false;
            boolean hasPengendalianNotif = false;

            if ("Admin".equals(user.getRole())) {
                hasReportNotif = riskProjectRepository.existsByNotifReportAdminTrue();
            } else if ("RiskOwner".equals(user.getRole())) {
                hasReportNotif = riskProjectRepository.existsByNotifReportRiskOwnerTrueAndRiskOwner(user.getNama());
            } else {
                hasIdentifikasiNotif = riskProjectRepository.existsByNotifIdentifikasiRiskOfficerTrueAndDibuatOleh(user.getNama());
                hasPengendalianNotif = riskProjectRepository.existsByNotifPengendalianRiskOfficerTrueAndDibuatOleh(user.getNama());
            }

            model.addAttribute("hasReportNotif", hasReportNotif);
            model.addAttribute("hasIdentifikasiNotif", hasIdentifikasiNotif);
            model.addAttribute("hasPengendalianNotif", hasPengendalianNotif);
        }
    }
}
