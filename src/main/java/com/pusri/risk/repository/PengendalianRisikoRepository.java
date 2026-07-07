package com.pusri.risk.repository;

import com.pusri.risk.model.PengendalianRisiko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PengendalianRisikoRepository extends JpaRepository<PengendalianRisiko, Long> {
}
