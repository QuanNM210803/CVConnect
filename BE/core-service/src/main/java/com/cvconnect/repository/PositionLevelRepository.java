package com.cvconnect.repository;

import com.cvconnect.entity.PositionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionLevelRepository extends JpaRepository<PositionLevel, Long> {
}
