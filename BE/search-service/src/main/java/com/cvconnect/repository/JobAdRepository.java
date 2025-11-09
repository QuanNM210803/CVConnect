package com.cvconnect.repository;

import com.cvconnect.document.JobAdDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobAdRepository extends ElasticsearchRepository<JobAdDocument, String> {
}
