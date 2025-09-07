package com.cvconnect.repository;

import com.cvconnect.entity.OrganizationAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgAddressRepository extends JpaRepository<OrganizationAddress, Long> {
}
