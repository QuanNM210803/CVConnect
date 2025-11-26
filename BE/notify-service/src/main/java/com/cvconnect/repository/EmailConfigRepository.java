package com.cvconnect.repository;

import com.cvconnect.entity.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {

    @Query("SELECT e FROM EmailConfig e " +
            "WHERE (:orgId IS NULL AND e.orgId IS NULL) " +
            "OR (:orgId IS NOT NULL AND e.orgId = :orgId)")
    EmailConfig findByOrgId(Long orgId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EmailConfig e WHERE e.orgId = :orgId")
    boolean existsByOrgId(Long orgId);

    @Query("SELECT e FROM EmailConfig e WHERE e.id = :id AND e.orgId = :orgId")
    EmailConfig findByIdAndOrgId(Long id, Long orgId);
}
