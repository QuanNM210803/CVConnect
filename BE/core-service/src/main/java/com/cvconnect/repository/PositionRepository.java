package com.cvconnect.repository;

import com.cvconnect.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Position p " +
            "WHERE p.code = :code AND p.departmentId = :departmentId")
    boolean existsByCodeAndDepartmentId(String code, Long departmentId);
}
