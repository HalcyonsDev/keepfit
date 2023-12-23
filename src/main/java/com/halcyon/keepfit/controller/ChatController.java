package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.dto.chat.NewChatMessageDto;
import com.halcyon.keepfit.model.chat.ChatMessage;
import com.halcyon.keepfit.model.chat.ChatNotification;
import com.halcyon.keepfit.service.chat.ChatMessageService;
import com.halcyon.keepfit.service.chat.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ChatMessage> processMessage(@RequestBody @Valid NewChatMessageDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        ChatMessage saved = chatMessageService.processMessage(dto);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getRecipientId()),
                "/queue/messages",
                new ChatNotification(saved.getId(), saved.getSenderId(), saved.getSenderName())
        );

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable Long senderId, @PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable Long id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }

    @GetMapping("/{senderId}/{recipientId}")
    private ResponseEntity<Page<ChatMessage>> getMessagesInChat(
            @PathVariable Long senderId,
            @PathVariable Long recipientId,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        Page<ChatMessage> messages = chatMessageService.findChatMessages(senderId, recipientId, offset, limit);
        return ResponseEntity.ok(messages);
    }
}
