package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.chat.ChatMessage;
import com.halcyon.keepfit.model.chat.MessageStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);
    Page<ChatMessage> findAllByChatId(String chatId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE ChatMessage msg SET msg.status = ?3 WHERE msg.senderId = ?1 AND msg.recipientId = ?2")
    void updateStatuses(Long senderId, Long recipientId, MessageStatus status);
}
