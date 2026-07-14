package com.pusri.risk.repository;

import com.pusri.risk.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    List<SystemLog> findAllByOrderByTimestampDesc();
    
    @Transactional
    @Modifying
    @Query("DELETE FROM SystemLog s WHERE s.actionType != 'CREATE_USER'")
    void deleteLogsExceptCreateUser();
}
