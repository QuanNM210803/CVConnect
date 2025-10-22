package com.cvconnect.repository;

import com.cvconnect.entity.EmailLog;
import com.cvconnect.enums.SendEmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    @Query("SELECT e FROM EmailLog e WHERE e.status = :status AND e.createdAt <= CURRENT_TIMESTAMP - 1 MINUTE " +
            "ORDER BY e.createdAt ASC LIMIT :limit")
    List<EmailLog> findByStatus(SendEmailStatus status, Long limit);

    @Query("SELECT e FROM EmailLog e WHERE e.candidateInfoId = :candidateInfoId AND e.orgId = :orgId ORDER BY e.createdAt DESC")
    List<EmailLog> findByCandidateInfoIdAndOrgId(Long candidateInfoId, Long orgId);
}
