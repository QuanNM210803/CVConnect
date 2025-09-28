package com.cvconnect.repository;

import com.cvconnect.entity.IndustrySub;
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
}
