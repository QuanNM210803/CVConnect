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
            "(:#{#request.code} IS NULL OR l.code LIKE %:#{#request.code}%) AND " +
            "(:#{#request.name} IS NULL OR l.name LIKE %:#{#request.name}%) AND " +
            "(:#{#request.createdAtStart} IS NULL OR l.createdAt >= :#{#request.createdAtStart}) AND " +
            "(:#{#request.createdAtEnd} IS NULL OR l.createdAt < :#{#request.createdAtEnd}) AND " +
            "(:#{#request.updatedAtStart} IS NULL OR l.updatedAt >= :#{#request.updatedAtStart}) AND " +
            "(:#{#request.updatedAtEnd} IS NULL OR l.updatedAt < :#{#request.updatedAtEnd}) AND " +
            "(:#{#request.createdBy} IS NULL OR l.createdBy LIKE %:#{#request.createdBy}%) AND " +
            "(:#{#request.updatedBy} IS NULL OR l.updatedBy LIKE %:#{#request.updatedBy}%)"
    )
    Page<Level> filter(LevelFilterRequest request, Pageable pageable);

    boolean existsByCode(@NotNull String code);
    Level findByCode(@NotNull String code);
}
