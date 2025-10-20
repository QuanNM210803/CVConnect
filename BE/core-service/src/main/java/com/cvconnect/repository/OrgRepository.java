package com.cvconnect.repository;

import com.cvconnect.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgRepository extends JpaRepository<Organization, Long> {
    @Modifying
    @Query("UPDATE Organization o SET o.createdBy = :createdBy WHERE o.id = :orgId")
    void updateCreatedBy(Long orgId, String createdBy);
}
