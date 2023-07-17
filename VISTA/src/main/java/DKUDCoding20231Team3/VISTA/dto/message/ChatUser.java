package DKUDCoding20231Team3.VISTA.dto.message;

import DKUDCoding20231Team3.VISTA.domain.entity.Chat;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.database.ChatInterface;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {

    private Long memberId;

    private String name;

    private Gender gender;

    private String image;

    private int count = 0;

    private List<ChatMessage> chatMessages;

    public static ChatUser of(ChatInterface chatInterface) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(ChatMessage.of(
                chatInterface.getSendMemberId(),
                chatInterface.getRecvMemberId(),
                chatInterface.getTimeStamp(),
                chatInterface.getMessage()));

        return ChatUser.builder()
                .memberId(chatInterface.getAns())
                .name(chatInterface.getName())
                .gender(chatInterface.getGender())
                .image(chatInterface.getImage())
                .count(1)
                .chatMessages(chatMessages).build();
    }

    public static ChatUser of(Long memberId, List<Chat> chats) {
        Collections.reverse(chats);
        return ChatUser.builder()
                .memberId(memberId)
                .count(chats.size())
                .chatMessages(ChatMessage.of(chats)).build();
    }

    public static ChatUser of(ChatMessage chatMessage) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);

        return ChatUser.builder()
                .memberId(chatMessage.getSendMemberId())
                .count(1)
                .chatMessages(chatMessages).build();
    }
}
