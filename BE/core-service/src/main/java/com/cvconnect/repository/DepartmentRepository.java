package com.cvconnect.repository;

import com.cvconnect.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Department d WHERE d.code = :code AND d.orgId = :orgId")
    boolean existsByCodeAndOrgId(String code, Long orgId);
}
