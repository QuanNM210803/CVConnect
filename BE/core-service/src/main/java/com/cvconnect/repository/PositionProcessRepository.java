package com.cvconnect.repository;

import com.cvconnect.entity.PositionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionProcessRepository extends JpaRepository<PositionProcess, Long> {
}
