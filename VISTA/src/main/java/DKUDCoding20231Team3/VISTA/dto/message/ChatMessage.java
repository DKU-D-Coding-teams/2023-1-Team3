package DKUDCoding20231Team3.VISTA.dto.message;

import DKUDCoding20231Team3.VISTA.domain.entity.Chat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private Long sendMemberId;

    private Long recvMemberId;

    private LocalDateTime timeStamp;

    private String message;

    public static ChatMessage of(Long sendMemberId, Long recvMemberId, LocalDateTime timeStamp, String message) {
        return ChatMessage.builder()
                .sendMemberId(sendMemberId)
                .recvMemberId(recvMemberId)
                .timeStamp(timeStamp)
                .message(message).build();
    }

    public static List<ChatMessage> of(List<Chat> chats) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        for(Chat chat : chats) {
            chatMessages.add(ChatMessage.of(
                    chat.getSendMemberId(),
                    chat.getRecvMemberId(),
                    chat.getTimeStamp(),
                    chat.getMessage()
            ));
        }
        return chatMessages;
    }

}
