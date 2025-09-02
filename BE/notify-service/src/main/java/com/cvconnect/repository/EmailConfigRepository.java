package com.cvconnect.repository;

import com.cvconnect.entity.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {

    @Query("SELECT e FROM EmailConfig e " +
            "WHERE (:orgId IS NULL AND e.orgId IS NULL) " +
            "OR (:orgId IS NOT NULL AND e.orgId = :orgId)")
    EmailConfig findByOrgId(Long orgId);
}
