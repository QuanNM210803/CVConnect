package com.cvconnect.repository;

import com.cvconnect.entity.CalendarCandidateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarCandidateInfoRepository extends JpaRepository<CalendarCandidateInfo, Long> {
}
