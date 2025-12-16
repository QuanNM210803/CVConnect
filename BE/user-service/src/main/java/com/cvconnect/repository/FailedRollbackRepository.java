package com.cvconnect.repository;

import com.cvconnect.entity.FailedRollback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedRollbackRepository extends JpaRepository<FailedRollback, Long> {
    @Query("SELECT fr FROM FailedRollback fr WHERE fr.status = false and fr.retryCount < 3")
    List<FailedRollback> getPendingFailedRollbacks();
}
