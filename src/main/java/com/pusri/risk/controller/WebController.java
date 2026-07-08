package com.pusri.risk.controller;

import com.pusri.risk.model.RiskProject;
import com.pusri.risk.model.User;
import com.pusri.risk.model.PengendalianRisiko;
import com.pusri.risk.model.RejectionHistory;
import com.pusri.risk.repository.RiskProjectRepository;
import com.pusri.risk.repository.PengendalianRisikoRepository;
import com.pusri.risk.repository.RejectionHistoryRepository;
import com.pusri.risk.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pusri.risk.model.RiskProjectHistory;
import com.pusri.risk.repository.RiskProjectHistoryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.time.LocalDate;

@Controller
public class WebController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RiskProjectRepository riskProjectRepository;
    
    @Autowired
    private PengendalianRisikoRepository pengendalianRisikoRepository;

    @Autowired
    private RejectionHistoryRepository rejectionHistoryRepository;
    
    @Autowired
    private RiskProjectHistoryRepository riskProjectHistoryRepository;
    
    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String loginId, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = userService.authenticate(loginId, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        }
        redirectAttributes.addFlashAttribute("error", "Invalid ID/Nama or password");
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) Integer year, 
                            @RequestParam(required = false) String quarter, 
                            HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        
        List<RiskProject> allProjects;
        
        if ("Admin".equals(user.getRole())) {
            allProjects = riskProjectRepository.findAll();
        } else if ("RiskOwner".equals(user.getRole())) {
            allProjects = riskProjectRepository.findByRiskOwner(user.getNama());
        } else {
            // RiskOfficer or others
            allProjects = riskProjectRepository.findByDibuatOleh(user.getNama());
        }
        
        // Filter in memory for year and quarter
        List<RiskProject> projects = allProjects.stream()
            .filter(p -> year == null || year.equals(p.getTahun()))
            .filter(p -> quarter == null || quarter.isEmpty() || quarter.equals(p.getKuartal()))
            .toList();
        
        // Pass current selected filters to the view
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedQuarter", quarter);
        
        long totalRisk = projects.size();
        long riskLow = projects.stream().filter(p -> "Low".equalsIgnoreCase(p.getRiskLevel())).count();
        long riskLowMod = projects.stream().filter(p -> "Low to Moderate".equalsIgnoreCase(p.getRiskLevel())).count();
        long riskMod = projects.stream().filter(p -> "Moderate".equalsIgnoreCase(p.getRiskLevel())).count();
        long riskModHigh = projects.stream().filter(p -> "Moderate to High".equalsIgnoreCase(p.getRiskLevel())).count();
        long riskHigh = projects.stream().filter(p -> "High".equalsIgnoreCase(p.getRiskLevel())).count();
        
        model.addAttribute("totalRisk", totalRisk);
        model.addAttribute("riskLow", riskLow);
        model.addAttribute("riskLowMod", riskLowMod);
        model.addAttribute("riskMod", riskMod);
        model.addAttribute("riskModHigh", riskModHigh);
        model.addAttribute("riskHigh", riskHigh);
        model.addAttribute("projects", projects);
        
        return "dashboard";
    }

    @GetMapping("/pengendalian")
    public String pengendalianPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || (!"Admin".equals(user.getRole()) && !"CORPORATE RISK OFFICER".equals(user.getRole()) && !"RiskOfficer".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        List<RiskProject> requestedProjects = riskProjectRepository.findByDibuatOlehAndUpdateTriwulanRequestedTrue(user.getNama());
        model.addAttribute("requestedProjects", requestedProjects);
        
        return "pengendalian";
    }
    
    @GetMapping("/identifikasi")
    public String identifikasiPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        if (!"Admin".equals(user.getRole()) && !"CORPORATE RISK OFFICER".equals(user.getRole()) && !"RiskOfficer".equals(user.getRole())) {
            return "redirect:/dashboard";
        }
        
        List<RiskProject> projects;
        if ("Admin".equals(user.getRole())) {
            projects = riskProjectRepository.findAll();
        } else if ("RiskOwner".equals(user.getRole())) {
            projects = riskProjectRepository.findByRiskOwner(user.getNama());
        } else {
            projects = riskProjectRepository.findByDibuatOleh(user.getNama());
        }
        
        List<User> riskOwners = userService.getAllUsers().stream()
                .filter(u -> "RiskOwner".equals(u.getRole()))
                .toList();
                
        List<RiskProject> allProjectsDb = riskProjectRepository.findAll();
                
        model.addAttribute("projects", projects);
        model.addAttribute("allProjects", allProjectsDb);
        model.addAttribute("riskOwners", riskOwners);
        return "identifikasi";
    }
    
    @PostMapping("/identifikasi/add")
    public String addIdentifikasi(HttpSession session,
                                  @RequestParam("idRisiko") String idRisiko,
                                  @RequestParam("sasaranUnitKerja") String sasaranUnitKerja,
                                  @RequestParam("konteksEksternal") String konteksEksternal,
                                  @RequestParam("konteksInternal") String konteksInternal,
                                  @RequestParam("risiko") String risiko,
                                  @RequestParam("kategoriRisiko") String kategoriRisiko,
                                  @RequestParam("penyebab") String penyebab,
                                  @RequestParam("dampak") String dampak,
                                  @RequestParam(value = "sasaranFile", required = false) MultipartFile sasaranFile,
                                  @RequestParam(value = "eksternalFile", required = false) MultipartFile eksternalFile,
                                  @RequestParam(value = "internalFile", required = false) MultipartFile internalFile,
                                  @RequestParam(value = "risikoFile", required = false) MultipartFile risikoFile,
                                  @RequestParam(value = "penyebabFile", required = false) MultipartFile penyebabFile,
                                  @RequestParam(value = "dampakFile", required = false) MultipartFile dampakFile,
                                  @RequestParam("existingControl") String existingControl,
                                  @RequestParam(value = "existingControlFile", required = false) MultipartFile existingControlFile,
                                  @RequestParam("rencanaPengendalian") String rencanaPengendalian,
                                  @RequestParam(value = "rencanaPengendalianFile", required = false) MultipartFile rencanaPengendalianFile,
                                  @RequestParam("riskOwner") String riskOwner,
                                  @RequestParam("peluangScore") Integer peluangScore,
                                  @RequestParam("dampakScore") Integer dampakScore,
                                  RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || (!"Admin".equals(user.getRole()) && !"CORPORATE RISK OFFICER".equals(user.getRole()) && !"RiskOfficer".equals(user.getRole()))) {
            return "redirect:/dashboard";
        }

        RiskProject project = new RiskProject();
        project.setIdRisiko(idRisiko);
        project.setNamaProject(idRisiko); // Default to ID Risiko
        project.setDibuatOleh(user.getNama());
        project.setUnitKerja(user.getDepartemen());
        project.setSasaranUnitKerja(sasaranUnitKerja);
        project.setKonteksEksternal(konteksEksternal);
        project.setKonteksInternal(konteksInternal);
        project.setRisiko(risiko);
        project.setKategoriRisiko(kategoriRisiko);
        project.setPenyebab(penyebab);
        project.setDampak(dampak);
        project.setExistingControl(existingControl);
        project.setRencanaPengendalian(rencanaPengendalian);
        project.setRiskOwner(riskOwner);
        project.setPeluangScore(peluangScore);
        project.setDampakScore(dampakScore);
        project.calculateRiskLevels();
        project.setStatus("Open");
        
        // Auto-assign Year and Quarter
        LocalDate date = LocalDate.now();
        project.setTahun(date.getYear());
        int month = date.getMonthValue();
        if (month <= 3) project.setKuartal("Q1");
        else if (month <= 6) project.setKuartal("Q2");
        else if (month <= 9) project.setKuartal("Q3");
        else project.setKuartal("Q4");

        // Save files
        project.setSasaranUnitKerjaFile(saveProjectFile(sasaranFile, "sasaran"));
        project.setKonteksEksternalFile(saveProjectFile(eksternalFile, "konteks_eksternal"));
        project.setKonteksInternalFile(saveProjectFile(internalFile, "konteks_internal"));
        project.setRisikoFile(saveProjectFile(risikoFile, "risiko"));
        project.setPenyebabFile(saveProjectFile(penyebabFile, "penyebab"));
        project.setDampakFile(saveProjectFile(dampakFile, "dampak"));
        project.setExistingControlFile(saveProjectFile(existingControlFile, "existing_control"));
        project.setRencanaPengendalianFile(saveProjectFile(rencanaPengendalianFile, "rencana_pengendalian"));

        riskProjectRepository.save(project);
        redirectAttributes.addFlashAttribute("successMessage", "Data Identifikasi Risiko berhasil ditambahkan!");
        
        return "redirect:/identifikasi";
    }

    @PostMapping("/identifikasi/edit")
    public String editIdentifikasi(HttpSession session,
                                   @RequestParam("id") Long id,
                                   @RequestParam("idRisiko") String idRisiko,
                                   @RequestParam("sasaranUnitKerja") String sasaranUnitKerja,
                                   @RequestParam("konteksEksternal") String konteksEksternal,
                                   @RequestParam("konteksInternal") String konteksInternal,
                                   @RequestParam("risiko") String risiko,
                                   @RequestParam("kategoriRisiko") String kategoriRisiko,
                                   @RequestParam("penyebab") String penyebab,
                                   @RequestParam("dampak") String dampak,
                                   @RequestParam(value = "sasaranFile", required = false) MultipartFile sasaranFile,
                                   @RequestParam(value = "eksternalFile", required = false) MultipartFile eksternalFile,
                                   @RequestParam(value = "internalFile", required = false) MultipartFile internalFile,
                                   @RequestParam(value = "risikoFile", required = false) MultipartFile risikoFile,
                                   @RequestParam(value = "penyebabFile", required = false) MultipartFile penyebabFile,
                                   @RequestParam(value = "dampakFile", required = false) MultipartFile dampakFile,
                                   @RequestParam("existingControl") String existingControl,
                                   @RequestParam(value = "existingControlFile", required = false) MultipartFile existingControlFile,
                                   @RequestParam("rencanaPengendalian") String rencanaPengendalian,
                                   @RequestParam(value = "rencanaPengendalianFile", required = false) MultipartFile rencanaPengendalianFile,
                                   @RequestParam("riskOwner") String riskOwner,
                                   @RequestParam("peluangScore") Integer peluangScore,
                                   @RequestParam("dampakScore") Integer dampakScore,
                                   RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || (!"Admin".equals(user.getRole()) && !"CORPORATE RISK OFFICER".equals(user.getRole()) && !"RiskOfficer".equals(user.getRole()))) {
            return "redirect:/dashboard";
        }

        RiskProject project = riskProjectRepository.findById(id).orElse(null);
        if (project != null) {
            // Verify ownership if not admin
            if (!"Admin".equals(user.getRole()) && !user.getNama().equals(project.getDibuatOleh())) {
                redirectAttributes.addFlashAttribute("error", "Anda tidak memiliki akses untuk mengedit proyek ini.");
                return "redirect:/identifikasi";
            }

            project.setIdRisiko(idRisiko);
            project.setNamaProject(idRisiko);
            project.setSasaranUnitKerja(sasaranUnitKerja);
            project.setKonteksEksternal(konteksEksternal);
            project.setKonteksInternal(konteksInternal);
            project.setRisiko(risiko);
            project.setKategoriRisiko(kategoriRisiko);
            project.setPenyebab(penyebab);
            project.setDampak(dampak);
            project.setExistingControl(existingControl);
            project.setRencanaPengendalian(rencanaPengendalian);
            project.setRiskOwner(riskOwner);
            project.setPeluangScore(peluangScore);
            project.setDampakScore(dampakScore);
            project.calculateRiskLevels();

            // Update files only if new ones are provided
            if (sasaranFile != null && !sasaranFile.isEmpty()) project.setSasaranUnitKerjaFile(saveProjectFile(sasaranFile, "sasaran"));
            if (eksternalFile != null && !eksternalFile.isEmpty()) project.setKonteksEksternalFile(saveProjectFile(eksternalFile, "konteks_eksternal"));
            if (internalFile != null && !internalFile.isEmpty()) project.setKonteksInternalFile(saveProjectFile(internalFile, "konteks_internal"));
            if (risikoFile != null && !risikoFile.isEmpty()) project.setRisikoFile(saveProjectFile(risikoFile, "risiko"));
            if (penyebabFile != null && !penyebabFile.isEmpty()) project.setPenyebabFile(saveProjectFile(penyebabFile, "penyebab"));
            if (dampakFile != null && !dampakFile.isEmpty()) project.setDampakFile(saveProjectFile(dampakFile, "dampak"));
            if (existingControlFile != null && !existingControlFile.isEmpty()) project.setExistingControlFile(saveProjectFile(existingControlFile, "existing_control"));
            if (rencanaPengendalianFile != null && !rencanaPengendalianFile.isEmpty()) project.setRencanaPengendalianFile(saveProjectFile(rencanaPengendalianFile, "rencana_pengendalian"));

            riskProjectRepository.save(project);
            
            riskProjectHistoryRepository.save(new RiskProjectHistory(project, "EDIT_REVISION"));
            
            redirectAttributes.addFlashAttribute("successMessage", "Data Identifikasi Risiko berhasil diubah!");
        }

        return "redirect:/identifikasi";
    }

    @PostMapping("/identifikasi/submit/{id}")
    public String submitIdentifikasi(HttpSession session, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || (!"Admin".equals(user.getRole()) && !"CORPORATE RISK OFFICER".equals(user.getRole()) && !"RiskOfficer".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        RiskProject project = riskProjectRepository.findById(id).orElse(null);
        if (project != null && ("tersimpan".equals(project.getApprovalStatus()) || "rejected".equals(project.getApprovalStatus()))) {
            project.setApprovalStatus("menunggu");
            project.setAdminApproval("pending");
            project.setRiskOwnerApproval("pending");
            project.setRejectionReason(null);
            riskProjectRepository.save(project);
            
            riskProjectHistoryRepository.save(new RiskProjectHistory(project, "SUBMIT_IDENTIFIKASI"));
            
            redirectAttributes.addFlashAttribute("successMessage", "Proyek berhasil di-submit untuk persetujuan!");
        }
        return "redirect:/identifikasi";
    }

    @GetMapping("/report")
    public String reportPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<RiskProject> projects;
        List<RiskProject> approvedProjects;
        List<RiskProjectHistory> historyProjects = null;
        List<PengendalianRisiko> pendingTriwulan = null;
        
        if ("Admin".equals(user.getRole()) || "CORPORATE RISK OFFICER".equals(user.getRole())) {
            projects = riskProjectRepository.findByApprovalStatus("menunggu");
            approvedProjects = riskProjectRepository.findByApprovalStatus("open");
            historyProjects = riskProjectHistoryRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "timestamp"));
            pendingTriwulan = pengendalianRisikoRepository.findByApprovalStatus("menunggu");
        } else if ("RiskOwner".equals(user.getRole())) {
            projects = riskProjectRepository.findByRiskOwnerAndApprovalStatus(user.getNama(), "menunggu");
            approvedProjects = riskProjectRepository.findByRiskOwnerAndApprovalStatus(user.getNama(), "open");
            historyProjects = riskProjectHistoryRepository.findByRiskOwnerOrderByTimestampDesc(user.getNama());
            pendingTriwulan = pengendalianRisikoRepository.findByRiskProjectRiskOwnerAndApprovalStatus(user.getNama(), "menunggu");
        } else {
            projects = riskProjectRepository.findByDibuatOlehAndApprovalStatusNot(user.getNama(), "tersimpan");
            approvedProjects = riskProjectRepository.findByDibuatOlehAndApprovalStatus(user.getNama(), "open");
            historyProjects = riskProjectHistoryRepository.findByDibuatOlehOrderByTimestampDesc(user.getNama());
            pendingTriwulan = pengendalianRisikoRepository.findByRiskProjectDibuatOlehAndApprovalStatus(user.getNama(), "menunggu");
        }

        model.addAttribute("projects", projects);
        model.addAttribute("approvedProjects", approvedProjects);
        model.addAttribute("historyProjects", historyProjects);
        model.addAttribute("pendingTriwulan", pendingTriwulan);
        
        return "report";
    }

    @PostMapping("/report/approve")
    public String approveProject(HttpSession session, @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        RiskProject project = riskProjectRepository.findById(id).orElse(null);
        if (project != null && "menunggu".equals(project.getApprovalStatus())) {
            if ("Admin".equals(user.getRole())) {
                project.setAdminApproval("approved");
            } else if ("RiskOwner".equals(user.getRole()) && user.getNama().equals(project.getRiskOwner())) {
                project.setRiskOwnerApproval("approved");
            }

            // Check if both approved
            if ("approved".equals(project.getAdminApproval()) && "approved".equals(project.getRiskOwnerApproval())) {
                project.setApprovalStatus("open");
                project.setStatus("Open"); // sync with general status
            }
            riskProjectRepository.save(project);
            
            List<RiskProjectHistory> histories = riskProjectHistoryRepository.findByProjectIdOrderByTimestampDesc(project.getId());
            if (!histories.isEmpty()) {
                RiskProjectHistory latest = histories.get(0);
                latest.setApprovalStatus(project.getApprovalStatus());
                latest.setAdminApproval(project.getAdminApproval());
                latest.setRiskOwnerApproval(project.getRiskOwnerApproval());
                riskProjectHistoryRepository.save(latest);
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Proyek berhasil disetujui.");
        }
        return "redirect:/report";
    }

    @PostMapping("/report/reject")
    public String rejectProject(HttpSession session, @RequestParam("id") Long id, @RequestParam("reason") String reason, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        RiskProject project = riskProjectRepository.findById(id).orElse(null);
        if (project != null && "menunggu".equals(project.getApprovalStatus())) {
            if ("Admin".equals(user.getRole())) {
                project.setAdminApproval("rejected");
            } else if ("RiskOwner".equals(user.getRole()) && user.getNama().equals(project.getRiskOwner())) {
                project.setRiskOwnerApproval("rejected");
            }

            project.setApprovalStatus("rejected");
            project.setRejectionReason(reason);
            project.setStatus("Closed"); // Optionally mark project itself as Closed
            
            riskProjectRepository.save(project);
            
            List<RiskProjectHistory> histories = riskProjectHistoryRepository.findByProjectIdOrderByTimestampDesc(project.getId());
            if (!histories.isEmpty()) {
                RiskProjectHistory latest = histories.get(0);
                latest.setApprovalStatus(project.getApprovalStatus());
                latest.setAdminApproval(project.getAdminApproval());
                latest.setRiskOwnerApproval(project.getRiskOwnerApproval());
                latest.setRejectionReason(reason);
                riskProjectHistoryRepository.save(latest);
            }
            
            RejectionHistory rh = new RejectionHistory();
            rh.setRiskProject(project);
            rh.setRejectionType("Identifikasi Risiko");
            rh.setRejectedByRole(user.getRole());
            rh.setRejectedByName(user.getNama());
            rh.setReason(reason);
            rh.setRejectedAt(java.time.LocalDateTime.now());
            rejectionHistoryRepository.save(rh);
            redirectAttributes.addFlashAttribute("error", "Proyek telah ditolak.");
        }
        return "redirect:/report";
    }

    private String saveProjectFile(MultipartFile file, String folder) {
        if (file != null && !file.isEmpty()) {
            try {
                String uploadDir = "uploads/" + folder + "/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                return uploadDir + filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("users", userService.getAllUsers());
        
        List<User> riskOwners = userService.getAllUsers().stream()
                .filter(u -> "RiskOwner".equals(u.getRole()))
                .toList();
        model.addAttribute("riskOwners", riskOwners);
        
        model.addAttribute("projects", riskProjectRepository.findAll());
        return "admin";
    }
    
    @PostMapping("/admin/project/edit")
    public String editProject(@RequestParam("id") Long id,
                              @RequestParam("namaProject") String namaProject,
                              @RequestParam("idRisiko") String idRisiko,
                              @RequestParam("dibuatOleh") String dibuatOleh,
                              @RequestParam("unitKerja") String unitKerja,
                              @RequestParam("sasaranUnitKerja") String sasaranUnitKerja,
                              @RequestParam("konteksEksternal") String konteksEksternal,
                              @RequestParam("konteksInternal") String konteksInternal,
                              @RequestParam("risiko") String risiko,
                              @RequestParam("kategoriRisiko") String kategoriRisiko,
                              @RequestParam("penyebab") String penyebab,
                              @RequestParam("dampak") String dampak,
                              @RequestParam("existingControl") String existingControl,
                              @RequestParam("rencanaPengendalian") String rencanaPengendalian,
                              @RequestParam("riskOwner") String riskOwner,
                              @RequestParam("tahun") Integer tahun,
                              @RequestParam("kuartal") String kuartal,
                              @RequestParam("peluangScore") Integer peluangScore,
                              @RequestParam("dampakScore") Integer dampakScore,
                              @RequestParam("status") String status,
                              HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            return "redirect:/dashboard";
        }
        
        try {
            RiskProject p = riskProjectRepository.findById(id).orElse(null);
            if(p != null) {
                p.setNamaProject(namaProject);
                p.setIdRisiko(idRisiko);
                p.setDibuatOleh(dibuatOleh);
                p.setUnitKerja(unitKerja);
                p.setSasaranUnitKerja(sasaranUnitKerja);
                p.setKonteksEksternal(konteksEksternal);
                p.setKonteksInternal(konteksInternal);
                p.setRisiko(risiko);
                p.setKategoriRisiko(kategoriRisiko);
                p.setPenyebab(penyebab);
                p.setDampak(dampak);
                p.setExistingControl(existingControl);
                p.setRencanaPengendalian(rencanaPengendalian);
                p.setRiskOwner(riskOwner);
                p.setTahun(tahun);
                p.setKuartal(kuartal);
                p.setPeluangScore(peluangScore);
                p.setDampakScore(dampakScore);
                p.setStatus(status);
                
                // Automatically recalculate based on the custom matrix
                p.calculateRiskLevels();
                
                riskProjectRepository.save(p);
                redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating project.");
        }
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/project/delete/{id}")
    public String deleteProject(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            return "redirect:/dashboard";
        }
        
        try {
            riskProjectRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting project.");
        }
        return "redirect:/admin";
    }
    
    @PostMapping("/admin/register")
    public String registerUser(User newUser, @RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            return "redirect:/dashboard";
        }
        
        try {
            if (file != null && !file.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                newUser.setFotoProfil(filename);
            }
            
            userService.registerAccount(newUser);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating account. Ensure email and badge ID are unique.");
        }
        
        return "redirect:/admin";
    }
    
    @PostMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, 
                           @RequestParam("email") String email,
                           @RequestParam("nama") String nama,
                           @RequestParam("badgeId") String badgeId,
                           @RequestParam("role") String role,
                           @RequestParam("departemen") String departemen,
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            return "redirect:/dashboard";
        }
        
        try {
            User existingUser = userService.findById(id);
            if(existingUser != null) {
                existingUser.setEmail(email);
                existingUser.setNama(nama);
                existingUser.setBadgeId(badgeId);
                existingUser.setRole(role);
                existingUser.setDepartemen(departemen);
                
                if (file != null && !file.isEmpty()) {
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                    existingUser.setFotoProfil(filename);
                }
                
                userService.updateUser(existingUser);
                redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully!");
                
                // If editing self, update session
                if(loggedInUser.getId().equals(id)) {
                    session.setAttribute("loggedInUser", existingUser);
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating account.");
        }
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            return "redirect:/dashboard";
        }
        
        try {
            if(loggedInUser.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete your own account.");
                return "redirect:/admin";
            }
            
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Account deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting account.");
        }
        
        return "redirect:/admin";
    }

    @PostMapping("/report/request-triwulan")
    public String requestTriwulan(HttpSession session, @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || "RiskOfficer".equals(user.getRole())) return "redirect:/login";

        RiskProject project = riskProjectRepository.findById(id).orElse(null);
        if (project != null && "open".equals(project.getApprovalStatus())) {
            project.setUpdateTriwulanRequested(true);
            riskProjectRepository.save(project);
            redirectAttributes.addFlashAttribute("successMessage", "Permintaan update triwulan berhasil dikirim ke Risk Officer.");
        }
        return "redirect:/report";
    }

    @PostMapping("/pengendalian/submit-triwulan")
    public String submitTriwulan(HttpSession session, 
                                 @RequestParam("projectId") Long projectId,
                                 @RequestParam("realisasiPengendalian") String realisasiPengendalian,
                                 @RequestParam("peluangScore") Integer peluangScore,
                                 @RequestParam("dampakScore") Integer dampakScore,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"RiskOfficer".equals(user.getRole())) return "redirect:/login";

        RiskProject project = riskProjectRepository.findById(projectId).orElse(null);
        if (project != null && project.isUpdateTriwulanRequested()) {
            PengendalianRisiko pr = new PengendalianRisiko();
            pr.setRealisasiPengendalian(realisasiPengendalian);
            pr.setPeluangScore(peluangScore);
            pr.setDampakScore(dampakScore);
            pr.calculateRiskMatrix();
            pr.setTriwulanKe(project.getPengendalianRisikoList().size() + 1);
            pr.setRiskProject(project);
            pr.setApprovalStatus("menunggu");
            pr.setAdminApproval("pending");
            pr.setRiskOwnerApproval("pending");

            pengendalianRisikoRepository.save(pr);

            project.setUpdateTriwulanRequested(false);
            riskProjectRepository.save(project);
            
            riskProjectHistoryRepository.save(new RiskProjectHistory(project, pr, "UPDATE_TRIWULAN_" + pr.getTriwulanKe()));
            
            redirectAttributes.addFlashAttribute("successMessage", "Update Triwulan berhasil di-submit untuk persetujuan!");
        }
        return "redirect:/pengendalian";
    }

    @PostMapping("/report/approve-triwulan")
    public String approveTriwulan(HttpSession session, @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || "RiskOfficer".equals(user.getRole())) return "redirect:/login";

        PengendalianRisiko pr = pengendalianRisikoRepository.findById(id).orElse(null);
        if (pr != null && "menunggu".equals(pr.getApprovalStatus())) {
            if ("Admin".equals(user.getRole())) {
                pr.setAdminApproval("approved");
            } else if ("RiskOwner".equals(user.getRole()) && user.getNama().equals(pr.getRiskProject().getRiskOwner())) {
                pr.setRiskOwnerApproval("approved");
            }

            if ("approved".equals(pr.getAdminApproval()) && "approved".equals(pr.getRiskOwnerApproval())) {
                pr.setApprovalStatus("open");
            }
            pengendalianRisikoRepository.save(pr);
            
            List<RiskProjectHistory> histories = riskProjectHistoryRepository.findByProjectIdOrderByTimestampDesc(pr.getRiskProject().getId());
            if (!histories.isEmpty()) {
                RiskProjectHistory latest = histories.get(0);
                if (latest.getActionType().startsWith("UPDATE_TRIWULAN_")) {
                    latest.setApprovalStatus(pr.getApprovalStatus());
                    latest.setAdminApproval(pr.getAdminApproval());
                    latest.setRiskOwnerApproval(pr.getRiskOwnerApproval());
                    riskProjectHistoryRepository.save(latest);
                }
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Update Triwulan berhasil disetujui.");
        }
        return "redirect:/report";
    }

    @PostMapping("/report/reject-triwulan")
    public String rejectTriwulan(HttpSession session, @RequestParam("id") Long id, @RequestParam("reason") String reason, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || "RiskOfficer".equals(user.getRole())) return "redirect:/login";

        PengendalianRisiko pr = pengendalianRisikoRepository.findById(id).orElse(null);
        if (pr != null && "menunggu".equals(pr.getApprovalStatus())) {
            if ("Admin".equals(user.getRole())) {
                pr.setAdminApproval("rejected");
            } else if ("RiskOwner".equals(user.getRole()) && user.getNama().equals(pr.getRiskProject().getRiskOwner())) {
                pr.setRiskOwnerApproval("rejected");
            }

            pr.setApprovalStatus("rejected");
            pr.setRejectionReason(reason);
            pengendalianRisikoRepository.save(pr);
            
            List<RiskProjectHistory> histories = riskProjectHistoryRepository.findByProjectIdOrderByTimestampDesc(pr.getRiskProject().getId());
            if (!histories.isEmpty()) {
                RiskProjectHistory latest = histories.get(0);
                if (latest.getActionType().startsWith("UPDATE_TRIWULAN_")) {
                    latest.setApprovalStatus(pr.getApprovalStatus());
                    latest.setAdminApproval(pr.getAdminApproval());
                    latest.setRiskOwnerApproval(pr.getRiskOwnerApproval());
                    latest.setRejectionReason(reason);
                    riskProjectHistoryRepository.save(latest);
                }
            }
            
            RejectionHistory rh = new RejectionHistory();
            rh.setRiskProject(pr.getRiskProject());
            rh.setRejectionType("Update Triwulan Ke-" + pr.getTriwulanKe());
            rh.setRejectedByRole(user.getRole());
            rh.setRejectedByName(user.getNama());
            rh.setReason(reason);
            rh.setRejectedAt(java.time.LocalDateTime.now());
            rejectionHistoryRepository.save(rh);
            
            // Allow RiskOfficer to resubmit
            pr.getRiskProject().setUpdateTriwulanRequested(true);
            riskProjectRepository.save(pr.getRiskProject());
            
            redirectAttributes.addFlashAttribute("errorMessage", "Update Triwulan ditolak.");
        }
        return "redirect:/report";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout-success";
    }

    @GetMapping("/debug")
    @ResponseBody
    public List<RiskProject> debugProjects() {
        return riskProjectRepository.findAll();
    }
}
