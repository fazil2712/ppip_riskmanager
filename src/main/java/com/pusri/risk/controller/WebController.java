package com.pusri.risk.controller;

import com.pusri.risk.model.RiskProject;
import com.pusri.risk.model.User;
import com.pusri.risk.repository.RiskProjectRepository;
import com.pusri.risk.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            allProjects = riskProjectRepository.findByUnitKerja(user.getDepartemen());
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
    public String pengendalianPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
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
        } else {
            projects = riskProjectRepository.findByDibuatOleh(user.getNama());
        }
        model.addAttribute("projects", projects);
        return "identifikasi";
    }
    
    @PostMapping("/identifikasi/add")
    public String addIdentifikasi(HttpSession session,
                                  @RequestParam("idRisiko") String idRisiko,
                                  @RequestParam("sasaranUnitKerja") String sasaranUnitKerja,
                                  @RequestParam("konteksEksternal") String konteksEksternal,
                                  @RequestParam("konteksInternal") String konteksInternal,
                                  @RequestParam("risiko") String risiko,
                                  @RequestParam(value = "sasaranFile", required = false) MultipartFile sasaranFile,
                                  @RequestParam(value = "eksternalFile", required = false) MultipartFile eksternalFile,
                                  @RequestParam(value = "internalFile", required = false) MultipartFile internalFile,
                                  @RequestParam(value = "risikoFile", required = false) MultipartFile risikoFile,
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
                                   @RequestParam(value = "sasaranFile", required = false) MultipartFile sasaranFile,
                                   @RequestParam(value = "eksternalFile", required = false) MultipartFile eksternalFile,
                                   @RequestParam(value = "internalFile", required = false) MultipartFile internalFile,
                                   @RequestParam(value = "risikoFile", required = false) MultipartFile risikoFile,
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

            // Update files only if new ones are provided
            if (sasaranFile != null && !sasaranFile.isEmpty()) project.setSasaranUnitKerjaFile(saveProjectFile(sasaranFile, "sasaran"));
            if (eksternalFile != null && !eksternalFile.isEmpty()) project.setKonteksEksternalFile(saveProjectFile(eksternalFile, "konteks_eksternal"));
            if (internalFile != null && !internalFile.isEmpty()) project.setKonteksInternalFile(saveProjectFile(internalFile, "konteks_internal"));
            if (risikoFile != null && !risikoFile.isEmpty()) project.setRisikoFile(saveProjectFile(risikoFile, "risiko"));

            riskProjectRepository.save(project);
            redirectAttributes.addFlashAttribute("successMessage", "Data Identifikasi Risiko berhasil diubah!");
        }

        return "redirect:/identifikasi";
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
                              @RequestParam("bidang") String bidang,
                              @RequestParam("penyebab") String penyebab,
                              @RequestParam("dampak") String dampak,
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
                p.setBidang(bidang);
                p.setPenyebab(penyebab);
                p.setDampak(dampak);
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

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout-success";
    }
}
