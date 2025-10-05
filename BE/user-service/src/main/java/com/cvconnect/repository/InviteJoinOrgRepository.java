package com.cvconnect.repository;

import com.cvconnect.entity.InviteJoinOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteJoinOrgRepository extends JpaRepository<InviteJoinOrg, Long> {
    @Query("SELECT i FROM InviteJoinOrg i WHERE i.token = :token")
    InviteJoinOrg findByToken(String token);
}
