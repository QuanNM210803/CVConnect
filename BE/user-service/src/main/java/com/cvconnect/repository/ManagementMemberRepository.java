package com.cvconnect.repository;

import com.cvconnect.entity.ManagementMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ManagementMemberRepository extends JpaRepository<ManagementMember, Long> {
}
