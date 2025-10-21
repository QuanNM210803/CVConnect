package com.cvconnect.service.impl;

import com.cvconnect.collection.Notification;
import com.cvconnect.dto.NotificationFilterRequest;
import com.cvconnect.service.MongoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MongoQueryServiceImpl implements MongoQueryService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Notification> findNotificationWithFilter(NotificationFilterRequest request, Pageable pageable) {
        Query query = new Query();

        if (request.getReceiverId() != null) {
            query.addCriteria(Criteria.where("receiverId").is(request.getReceiverId()));
        }

        if (request.getIsRead() != null) {
            query.addCriteria(Criteria.where("isRead").is(request.getIsRead()));
        }

        if (StringUtils.hasText(request.getType())) {
            query.addCriteria(Criteria.where("type").is(request.getType()));
        }

        if (request.getCreatedAtStart() != null && request.getCreatedAtEnd() != null) {
            query.addCriteria(Criteria.where("createdAt").gte(request.getCreatedAtStart()).lte(request.getCreatedAtEnd()));
        } else if (request.getCreatedAtStart() != null) {
            query.addCriteria(Criteria.where("createdAt").gte(request.getCreatedAtStart()));
        } else if (request.getCreatedAtEnd() != null) {
            query.addCriteria(Criteria.where("createdAt").lte(request.getCreatedAtEnd()));
        }
        long total = mongoTemplate.count(query, Notification.class);

        query.with(pageable);
        List<Notification> list = mongoTemplate.find(query, Notification.class);

        return new PageImpl<>(list, pageable, total);
    }
}
