package com.cvconnect.repository;

import com.cvconnect.entity.InterviewPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewPanelRepository extends JpaRepository<InterviewPanel, Long> {
}
