package com.cvconnect.repository;

import com.cvconnect.collection.Conversation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    @Query("{ 'jobAdId': ?0, 'candidateId': ?1 }")
    Conversation findByJobAdIdAndCandidateId(Long jobAdId, Long candidateId);

    @Aggregation(pipeline = {
            "{ $match: { 'participantIds': { $in: [?0] } } }",
            "{ $unwind: '$messages' }",
            "{ $match: { " +
                    "'messages.senderId': { $ne: ?0 }, " +
                    "'messages.seenBy': { $not: { $elemMatch: { 'userId': ?0 } } }" +
                    "} }"
    })
    List<Conversation> findAnyUnreadMessage(Long userId);
}
