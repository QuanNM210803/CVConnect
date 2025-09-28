package com.cvconnect.repository;

import com.cvconnect.dto.PositionLevelProjection;
import com.cvconnect.entity.PositionLevel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionLevelRepository extends JpaRepository<PositionLevel, Long> {
    @Query("SELECT DISTINCT pl.id AS id, pl.name AS name, pl.positionId AS positionId, l.id AS levelId, l.name AS levelName, l.code AS levelCode " +
            "FROM PositionLevel pl " +
            "JOIN Level l ON l.id = pl.levelId " +
            "WHERE pl.positionId = :positionId")
    List<PositionLevelProjection> findByPositionId(Long positionId);

    @Query("SELECT DISTINCT pl.id AS id, pl.name AS name, pl.positionId AS positionId, l.id AS levelId, l.name AS levelName, l.code AS levelCode " +
            "FROM PositionLevel pl " +
            "JOIN Level l ON l.id = pl.levelId " +
            "WHERE pl.positionId IN :positionIds")
    List<PositionLevelProjection> findByPositionIds(List<Long> positionIds);
}
