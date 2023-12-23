package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
