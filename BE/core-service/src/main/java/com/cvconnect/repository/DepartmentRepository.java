package com.cvconnect.repository;

import com.cvconnect.dto.department.DepartmentFilterRequest;
import com.cvconnect.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Department d WHERE d.code = :code AND d.orgId = :orgId")
    boolean existsByCodeAndOrgId(String code, Long orgId);

    @Query("SELECT d FROM Department d WHERE d.id = :id")
    Optional<Department> findById(Long id);

    @Query("SELECT d FROM Department d WHERE d.id IN :ids")
    List<Department> findAllById(List<Long> ids);

    @Query("SELECT d FROM Department d WHERE " +
            "(:#{#request.code} IS NULL OR LOWER(d.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.isActive} IS NULL OR d.isActive = :#{#request.isActive}) " +
            "AND (:#{#request.createdAtStart} IS NULL OR d.createdAt >= :#{#request.createdAtStart}) " +
            "AND (:#{#request.createdAtEnd} IS NULL OR d.createdAt <= :#{#request.createdAtEnd}) " +
            "AND (:#{#request.updatedAtStart} IS NULL OR d.updatedAt >= :#{#request.updatedAtStart}) " +
            "AND (:#{#request.updatedAtEnd} IS NULL OR d.updatedAt <= :#{#request.updatedAtEnd}) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(d.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(d.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) " +
            "AND (:#{#request.orgId} IS NULL OR d.orgId = :#{#request.orgId})"
    )
    Page<Department> filter(DepartmentFilterRequest request, Pageable pageable);
}
