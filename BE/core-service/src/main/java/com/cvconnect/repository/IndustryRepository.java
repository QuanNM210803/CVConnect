package com.cvconnect.repository;

import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.entity.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {

    @Query("SELECT i FROM Industry i WHERE " +
            "(:#{#request.code} IS NULL OR LOWER(i.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.createdAtStart} IS NULL OR i.createdAt >= :#{#request.createdAtStart}) " +
            "AND (:#{#request.createdAtEnd} IS NULL OR i.createdAt < :#{#request.createdAtEnd}) " +
            "AND (:#{#request.updatedAtStart} IS NULL OR i.updatedAt >= :#{#request.updatedAtStart}) " +
            "AND (:#{#request.updatedAtEnd} IS NULL OR i.updatedAt < :#{#request.updatedAtEnd}) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(i.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(i.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%')))"
    )
    Page<Industry> filter(IndustryFilterRequest request, Pageable pageable);
}
