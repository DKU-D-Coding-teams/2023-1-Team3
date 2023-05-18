package DKUDCoding20231Team3.VISTA.dto.message;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatMessageResponse {

    private String status;

    private int count = 0;

    private List<ChatMessage> chatMessages;

    public static ChatMessageResponse of(String status, int count, List<ChatMessage> chatMessages) {
        return ChatMessageResponse.builder()
                .status(status)
                .count(count)
                .chatMessages(chatMessages)
                .build();
    }

    public static ChatMessageResponse of(String status, int count, ChatMessage chatMessage) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);

        return ChatMessageResponse.builder()
                .status(status)
                .count(count)
                .chatMessages(chatMessages)
                .build();
    }
}
