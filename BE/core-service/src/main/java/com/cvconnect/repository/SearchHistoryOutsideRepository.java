package com.cvconnect.repository;

import com.cvconnect.entity.SearchHistoryOutside;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SearchHistoryOutsideRepository extends JpaRepository<SearchHistoryOutside, Long> {

    @Query("SELECT s FROM SearchHistoryOutside s WHERE s.userId = :userId ORDER BY s.createdAt DESC")
    List<SearchHistoryOutside> findByUserId(Long userId);

    @Query("SELECT s FROM SearchHistoryOutside s WHERE s.id IN :ids AND s.userId = :userId")
    List<SearchHistoryOutside> findByIds(List<Long> ids, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM SearchHistoryOutside s WHERE s.userId = :userId")
    void deleteByUserId(Long userId);
}
