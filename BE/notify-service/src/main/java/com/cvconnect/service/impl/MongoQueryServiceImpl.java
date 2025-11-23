package com.cvconnect.service.impl;

import com.cvconnect.collection.ChatMessage;
import com.cvconnect.collection.Notification;
import com.cvconnect.dto.ChatMessageFilter;
import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.NotificationFilterRequest;
import com.cvconnect.dto.internal.request.MyConversationWithFilter;
import com.cvconnect.service.MongoQueryService;
import nmquan.commonlib.dto.PageInfo;
import nmquan.commonlib.dto.response.FilterResponse;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

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

    @Override
    public FilterResponse<ConversationDto> getMyConversationsWithFilter(MyConversationWithFilter filter, Instant pageIndex, Integer pageSize) {

        List<AggregationOperation> ops = new ArrayList<>();

        ops.add(Aggregation.match(Criteria.where("participantIds").in(filter.getUserId())));
        ops.add(Aggregation.match(Criteria.where("candidateId").ne(filter.getUserId())));
        ops.add(Aggregation.match(Criteria.where("messages").exists(true).not().size(0)));

        if (filter.getHasMessagesUnread() != null) {
            if (filter.getHasMessagesUnread()) {
                ops.add(Aggregation.unwind("messages"));
                ops.add(Aggregation.sort(Sort.by(Sort.Direction.DESC,"messages.sentAt")));
                Criteria unreadCriteria = new Criteria().andOperator(
                        Criteria.where("messages.senderId").ne(filter.getUserId()),
                        Criteria.where("messages.seenBy").not().in(filter.getUserId())
                );
                ops.add(Aggregation.match(unreadCriteria));
            } else {
                Criteria readAllCriteria =
                        Criteria.where("messages").not().elemMatch(
                                new Criteria().andOperator(
                                        Criteria.where("senderId").ne(filter.getUserId()),
                                        Criteria.where("seenBy").not().in(filter.getUserId())
                                )
                        );
                ops.add(Aggregation.match(readAllCriteria));
                ops.add(Aggregation.unwind("messages"));
                ops.add(Aggregation.sort(Sort.by(Sort.Direction.DESC,"messages.sentAt")));
            }
        } else {
            ops.add(Aggregation.unwind("messages"));
            ops.add(Aggregation.sort(Sort.by(Sort.Direction.DESC,"messages.sentAt")));
        }

        ops.add(Aggregation.group("_id")
                .first("jobAdId").as("jobAdId")
                .first("candidateId").as("candidateId")
                .first("participantIds").as("participantIds")
                .max("messages.sentAt").as("lastMessageSentAt")
                .first("messages.text").as("lastMessage")
                .first("messages.senderId").as("lastMessageSenderId")
                .first("messages.seenBy").as("lastMessageSeenBy")
                .first("createdBy").as("createdBy")
                .first("createdAt").as("createdAt"));

        ops.add(Aggregation.match(Criteria.where("lastMessageSentAt").lt(Date.from(pageIndex))));
        ops.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "lastMessageSentAt")));
        ops.add(Aggregation.limit(pageSize));

        Aggregation agg = Aggregation.newAggregation(ops)
                .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());

        List<ConversationDto> content = mongoTemplate
                .aggregate(agg, "conversations", ConversationDto.class)
                .getMappedResults();

        // Tính tổng phần tử
        List<AggregationOperation> countOps = new ArrayList<>(ops.subList(0, ops.size() - 2)); // bỏ sort + limit cuối
        countOps.add(Aggregation.count().as("total"));

        Aggregation countAgg = Aggregation.newAggregation(countOps);

        long total = mongoTemplate.aggregate(countAgg, "conversations", Document.class).getUniqueMappedResult() != null ?
                mongoTemplate.aggregate(countAgg, "conversations", Document.class).getUniqueMappedResult().getInteger("total") : 0L;

        PageInfo pageInfo = PageInfo.builder()
                .totalElements(total)
                .build();
        FilterResponse<ConversationDto> response = new FilterResponse<>();
        response.setData(content);
        response.setPageInfo(pageInfo);
        return response;
    }

    @Override
    public ConversationDto getChatMessages(ChatMessageFilter filter) {
        AggregationOperation matchStage = Aggregation.match(
                Criteria.where("jobAdId").is(filter.getJobAdId())
                        .and("candidateId").is(filter.getCandidateId())
        );

        AggregationOperation filteredMessagesStage = context ->
                new Document("$addFields",
                        new Document("filteredMessages",
                                new Document("$filter",
                                        new Document("input", "$messages")
                                                .append("as", "msg")
                                                .append("cond",
                                                        new Document("$lt",
                                                                Arrays.asList("$$msg.sentAt",
                                                                        Date.from(filter.getPageIndex()))
                                                        )
                                                )
                                )
                        )
                );

        AggregationOperation sortedMessagesStage = context ->
                new Document("$addFields",
                        new Document("sortedMessages",
                                new Document("$sortArray",
                                        new Document("input", "$filteredMessages")
                                                .append("sortBy", new Document("sentAt", -1))
                                )
                        )
                );

        AggregationOperation sliceMessagesStage = context ->
                new Document("$addFields",
                        new Document("messages",
                                new Document("$slice",
                                        Arrays.asList("$sortedMessages", 0, filter.getPageSize())
                                )
                        )
                );

        AggregationOperation removeTempFields = context ->
                new Document("$project",
                        new Document("filteredMessages", 0)
                                .append("sortedMessages", 0)
                );

        Aggregation aggregation = Aggregation.newAggregation(
                matchStage,
                filteredMessagesStage,
                sortedMessagesStage,
                sliceMessagesStage,
                removeTempFields
        );

        AggregationResults<ConversationDto> results =
                mongoTemplate.aggregate(aggregation, "conversations", ConversationDto.class);

        ConversationDto result = results.getUniqueMappedResult();

        if (result != null) {
            FilterResponse<ChatMessage> chatMessages = new FilterResponse<>();

            List<ChatMessage> reversed = new ArrayList<>(result.getMessages());
            Collections.reverse(reversed);
            chatMessages.setData(reversed);

            result.setMessagesWithFilter(chatMessages);
            result.setMessages(null);
        }

        return result;
    }
}
