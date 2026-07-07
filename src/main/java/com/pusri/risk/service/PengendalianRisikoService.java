package com.pusri.risk.service;

import com.pusri.risk.model.PengendalianRisiko;
import com.pusri.risk.repository.PengendalianRisikoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PengendalianRisikoService {

    @Autowired
    private PengendalianRisikoRepository pengendalianRisikoRepository;

    public void simpanPengendalian(PengendalianRisiko pengendalian) {
        pengendalianRisikoRepository.save(pengendalian);
    }
}
