package com.cvconnect.repository;

import com.cvconnect.dto.level.LevelFilterRequest;
import com.cvconnect.entity.Level;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    @Query("SELECT l FROM Level l WHERE " +
            "(:#{#request.code} IS NULL OR LOWER(l.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (l.createdAt >= COALESCE(:#{#request.createdAtStart}, l.createdAt)) " +
            "AND (l.createdAt <= COALESCE(:#{#request.createdAtEnd}, l.createdAt)) " +
            "AND (COALESCE(:#{#request.updatedAtStart}, NULL) IS NULL OR (l.updatedAt IS NOT NULL AND l.updatedAt >= :#{#request.updatedAtStart})) " +
            "AND (COALESCE(:#{#request.updatedAtEnd}, NULL) IS NULL OR (l.updatedAt IS NOT NULL AND l.updatedAt <= :#{#request.updatedAtEnd})) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(l.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(l.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%')))"
    )
    Page<Level> filter(LevelFilterRequest request, Pageable pageable);

    boolean existsByCode(@NotNull String code);
    Level findByCode(@NotNull String code);
}
