package com.halcyon.keepfit.model.chat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;
}
