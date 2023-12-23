package com.halcyon.keepfit.model.chat;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "chat_messages")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "status")
    private MessageStatus status;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;
}
