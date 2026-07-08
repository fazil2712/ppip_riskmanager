package com.pusri.risk.repository;

import com.pusri.risk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByBadgeId(String badgeId);
    User findFirstByNama(String nama);
    List<User> findByRole(String role);
}
