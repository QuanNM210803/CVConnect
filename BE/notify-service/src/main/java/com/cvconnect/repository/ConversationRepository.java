package com.cvconnect.repository;

import com.cvconnect.collection.Conversation;
import com.cvconnect.dto.ConversationDto;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    @Query("{ 'jobAdId': ?0, 'candidateId': ?1 }")
    Conversation findByJobAdIdAndCandidateId(Long jobAdId, Long candidateId);

    @Aggregation(pipeline = {
            """
            { $match: { candidateId: ?0 } }
            """,
            """
            { $unwind: '$messages' }
            """,
            """
            {
              $match: {
                'messages.senderId': { $ne: ?0 },
                'messages.seenBy': { $not: { $in: [?0] } }
              }
            }
            """,
            """
            {
              $group: {
                _id: '$_id',
                jobAdId: { $first: '$jobAdId' },
                candidateId: { $first: '$candidateId' },
                participantIds: { $first: '$participantIds' },
                messages: { $push: '$messages' },
                createdBy: { $first: '$createdBy' },
                createdAt: { $first: '$createdAt' }
              }
            }
            """
    })
    List<Conversation> findAnyUnreadMessageCandidate(Long candidateId);

    @Aggregation(pipeline = {
            """
            {
              $match: {
                candidateId: { $ne: ?0 },
                participantIds: { $in: [?0] }
              }
            }
            """,
            """
            { $unwind: '$messages' }
            """,
            """
            {
              $match: {
                'messages.senderId': { $ne: ?0 },
                'messages.seenBy': { $not: { $in: [?0] } }
              }
            }
            """,
            """
            {
              $group: {
                _id: '$_id',
                jobAdId: { $first: '$jobAdId' },
                candidateId: { $first: '$candidateId' },
                participantIds: { $first: '$participantIds' },
                messages: { $push: '$messages' },
                createdBy: { $first: '$createdBy' },
                createdAt: { $first: '$createdAt' }
              }
            }
            """
    })
    List<Conversation> findAnyUnreadMessageHr(Long hrContactId);

    @Aggregation(pipeline = {
            """
            { $match: { candidateId: ?0 } }
            """,
            """
            { $unwind: "$messages" }
            """,
            """
            { $sort: { "messages.sentAt": -1 } }
            """,
            """
            {
                $group: {
                    _id: "$_id",
                    jobAdId: { $first: "$jobAdId" },
                    candidateId: { $first: "$candidateId" },
                    participantIds: { $first: "$participantIds" },
                    lastMessageSentAt: { $max: "$messages.sentAt" },
                    lastMessage: { $first: "$messages.text" },
                    lastMessageSenderId: { $first: "$messages.senderId" },
                    lastMessageSeenBy: { $first: "$messages.seenBy" },
                    createdBy: { $first: '$createdBy' },
                    createdAt: { $first: "$createdAt" }
                }
            }
            """,
            """
            { $sort: { lastMessageSentAt: -1 } }
            """
    })
    List<ConversationDto> findConversationByCandidate(Long candidateId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$addToSet': { 'messages.$[].seenBy': ?1 } }")
    void markAllMessagesAsRead(String conversationId, Long userId);

}
