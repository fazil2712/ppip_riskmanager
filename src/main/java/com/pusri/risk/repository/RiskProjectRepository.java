package com.pusri.risk.repository;

import com.pusri.risk.model.RiskProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskProjectRepository extends JpaRepository<RiskProject, Long> {
    List<RiskProject> findByDibuatOleh(String dibuatOleh);
    List<RiskProject> findByUnitKerja(String unitKerja);
    
    List<RiskProject> findByTahun(Integer tahun);
    List<RiskProject> findByDibuatOlehAndTahun(String dibuatOleh, Integer tahun);
    List<RiskProject> findByUnitKerjaAndTahun(String unitKerja, Integer tahun);
}
