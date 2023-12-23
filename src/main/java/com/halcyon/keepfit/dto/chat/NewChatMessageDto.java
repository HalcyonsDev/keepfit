package com.halcyon.keepfit.dto.chat;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewChatMessageDto {
    private Long recipientId;

    @Size(min = 2, max = 128, message = "Message content must be greater than 1 character and less than 128 characters.")
    private String content;
}
