package com.cvconnect.repository;

import com.cvconnect.entity.OrgMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgMemberRepository extends JpaRepository<OrgMember, Long> {
}
