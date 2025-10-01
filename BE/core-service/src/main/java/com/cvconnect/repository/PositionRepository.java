package com.cvconnect.repository;

import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.dto.position.PositionFilterRequest;
import com.cvconnect.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Position p " +
            "WHERE p.code = :code AND p.departmentId = :departmentId")
    boolean existsByCodeAndDepartmentId(String code, Long departmentId);

    @Query("SELECT DISTINCT p FROM Position p " +
            "JOIN Department d ON d.id = p.departmentId " +
            "JOIN Organization o ON o.id = d.orgId AND o.id = :orgId " +
            "WHERE p.id IN :ids")
    List<Position> findByIdsAndOrgId(List<Long> ids, Long orgId);

    @Query("SELECT p FROM Position p " +
            "JOIN Department d ON d.id = p.departmentId " +
            "JOIN Organization o ON o.id = d.orgId AND o.id = :orgId " +
            "WHERE p.id = :id")
    Position findByIdAndOrgId(Long id, Long orgId);

    @Query("SELECT DISTINCT new com.cvconnect.dto.position.PositionDto(p.id, p.name, p.code, p.isActive, p.createdBy, p.updatedBy, p.createdAt, p.updatedAt, " +
            "d.id, d.name, d.code) FROM Position p " +
            "JOIN Department d ON d.id = p.departmentId " +
            "JOIN Organization o ON o.id = d.orgId AND o.id = :#{#request.orgId} " +
            "JOIN PositionLevel pl ON pl.positionId = p.id " +
            "WHERE (:#{#request.code} IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.isActive} IS NULL OR p.isActive = :#{#request.isActive}) " +
            "AND (p.createdAt >= COALESCE(:#{#request.createdAtStart}, p.createdAt)) " +
            "AND (p.createdAt <= COALESCE(:#{#request.createdAtEnd}, p.createdAt)) " +
            "AND (COALESCE(:#{#request.updatedAtStart}, NULL) IS NULL OR (p.updatedAt IS NOT NULL AND p.updatedAt >= :#{#request.updatedAtStart})) " +
            "AND (COALESCE(:#{#request.updatedAtEnd}, NULL) IS NULL OR (p.updatedAt IS NOT NULL AND p.updatedAt <= :#{#request.updatedAtEnd})) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(p.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(p.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) " +
            "AND (:#{#request.departmentIds} IS NULL OR p.departmentId IN :#{#request.departmentIds}) " +
            "AND (:#{#request.positionLevelName} IS NULL OR LOWER(pl.name) LIKE LOWER(CONCAT('%', :#{#request.positionLevelName}, '%')))"
    )
    Page<PositionDto> filter(PositionFilterRequest request, Pageable pageable);
}
