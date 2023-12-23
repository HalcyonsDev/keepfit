package com.halcyon.keepfit.model.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_name")
    private String senderName;
}
