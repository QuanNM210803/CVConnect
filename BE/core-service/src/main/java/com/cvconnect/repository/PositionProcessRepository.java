package com.cvconnect.repository;

import com.cvconnect.dto.positionProcess.PositionProcessProjection;
import com.cvconnect.entity.PositionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionProcessRepository extends JpaRepository<PositionProcess, Long> {

    @Query("SELECT DISTINCT pp.id AS id, pp.name AS name, pp.positionId AS positionId, pp.sortOrder AS sortOrder, pt.id AS processId, pt.name AS processName, pt.code AS processCode " +
            "FROM PositionProcess pp " +
            "JOIN ProcessType pt ON pt.id = pp.processTypeId " +
            "WHERE pp.positionId = :positionId " +
            "ORDER BY sortOrder ASC")
    List<PositionProcessProjection> findByPositionId(Long positionId);
}
