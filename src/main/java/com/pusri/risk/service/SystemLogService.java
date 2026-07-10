package com.pusri.risk.service;

import com.pusri.risk.model.SystemLog;
import com.pusri.risk.model.User;
import com.pusri.risk.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    public void logAction(User actor, String actionType, String description) {
        if (actor == null) return;
        
        String actorString = actor.getNama() + " (" + actor.getBadgeId() + ")";
        SystemLog log = new SystemLog(actorString, actionType, description);
        systemLogRepository.save(log);
    }

    public java.util.List<SystemLog> getAllLogs() {
        return systemLogRepository.findAllByOrderByTimestampDesc();
    }
}
