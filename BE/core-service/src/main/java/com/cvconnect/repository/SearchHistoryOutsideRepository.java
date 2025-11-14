package com.cvconnect.repository;

import com.cvconnect.entity.SearchHistoryOutside;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryOutsideRepository extends JpaRepository<SearchHistoryOutside, Long> {

    @Query("SELECT s FROM SearchHistoryOutside s WHERE s.userId = :userId ORDER BY s.createdAt DESC LIMIT :limit")
    List<SearchHistoryOutside> findByUserIdLimit(Long userId, Long limit);
}
