package com.halcyon.keepfit.service.chat;

import com.halcyon.keepfit.dto.chat.NewChatMessageDto;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.model.chat.ChatMessage;
import com.halcyon.keepfit.model.chat.MessageStatus;
import com.halcyon.keepfit.repository.IChatMessageRepository;
import com.halcyon.keepfit.service.auth.AuthService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final IChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final AuthService authService;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage processMessage(NewChatMessageDto dto) {
        User sender = userService.findByEmail(authService.getAuthInfo().getEmail());
        User recipient = userService.findById(dto.getRecipientId());

        String chatId = chatRoomService.getChatId(sender.getId(), dto.getRecipientId(), true);
        ChatMessage message = ChatMessage.builder()
                .chatId(chatId)
                .senderId(sender.getId())
                .recipientId(dto.getRecipientId())
                .senderName(sender.getFirstname() + " " + sender.getLastname())
                .recipientName(recipient.getFirstname() + " " + recipient.getLastname())
                .content(dto.getContent())
                .timestamp(new Date())
                .build();

        return save(message);
    }

    public Long countNewMessages(Long senderId, Long recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    public Page<ChatMessage> findChatMessages(Long senderId, Long recipientId, Integer offset, Integer limit) {
        String chatId = chatRoomService.getChatId(senderId, recipientId, false);

        Page<ChatMessage> messages = chatMessageRepository.findAllByChatId(chatId,
                PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "id")));

        if (messages.getSize() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository
                .findById(id)
                .map(msg -> {
                    msg.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(msg);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat message with this id not found."));
    }

    private void updateStatuses(Long senderId, Long recipientId, MessageStatus status) {
        chatMessageRepository.updateStatuses(senderId, recipientId, status);
    }
}
