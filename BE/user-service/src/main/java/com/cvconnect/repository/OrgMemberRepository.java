package com.cvconnect.repository;

import com.cvconnect.entity.OrgMember;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgMemberRepository extends JpaRepository<OrgMember, Long> {
    boolean existsByUserId(@NotNull Long userId);

    Optional<OrgMember> findByUserId(@NotNull Long userId);
}
