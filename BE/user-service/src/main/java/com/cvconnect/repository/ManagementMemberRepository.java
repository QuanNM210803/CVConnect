package com.cvconnect.repository;

import com.cvconnect.entity.ManagementMember;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ManagementMemberRepository extends JpaRepository<ManagementMember, Long> {
    Optional<ManagementMember> findByUserId(@NotNull Long userId);
}
