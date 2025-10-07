package com.cvconnect.repository;

import com.cvconnect.dto.industrySub.IndustrySubFilterRequest;
import com.cvconnect.entity.IndustrySub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IndustrySubRepository extends JpaRepository<IndustrySub, Long> {
    @Query("SELECT is FROM IndustrySub is WHERE is.industryId IN :industryIds")
    List<IndustrySub> findByIndustryIds(List<Long> industryIds);

    List<IndustrySub> findAllByCodeIn(Collection<String> codes);

    @Query("SELECT is FROM IndustrySub is " +
            "WHERE (:#{#request.code} IS NULL OR LOWER(is.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(is.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.industryIds} IS NULL OR is.industryId IN :#{#request.industryIds}) " +
            "AND (is.createdAt >= COALESCE(:#{#request.createdAtStart}, is.createdAt)) " +
            "AND (is.createdAt <= COALESCE(:#{#request.createdAtEnd}, is.createdAt)) " +
            "AND (COALESCE(:#{#request.updatedAtStart}, NULL) IS NULL OR (is.updatedAt IS NOT NULL AND is.updatedAt >= :#{#request.updatedAtStart})) " +
            "AND (COALESCE(:#{#request.updatedAtEnd}, NULL) IS NULL OR (is.updatedAt IS NOT NULL AND is.updatedAt <= :#{#request.updatedAtEnd})) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(is.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(is.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) "
    )
    Page<IndustrySub> filter(IndustrySubFilterRequest request, Pageable pageable);
}
