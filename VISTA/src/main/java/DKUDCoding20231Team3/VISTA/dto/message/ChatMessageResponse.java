package DKUDCoding20231Team3.VISTA.dto.message;

import DKUDCoding20231Team3.VISTA.domain.entity.Chat;
import DKUDCoding20231Team3.VISTA.dto.database.ChatInterface;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatMessageResponse {

    private String status;

    private int count = 0;

    private List<ChatUser> chatUsers = new ArrayList<>();

    public ChatMessageResponse(String status, List<ChatMessage> chatMessages) {
        for(ChatMessage chatMessage : chatMessages) {
            ChatUser chatUser = getChatUserByMemberId(chatMessage.getSendMemberId());
            chatUser.getChatMessages().add(chatMessage);
            chatUser.setCount(chatUser.getCount() + 1);
        }
        this.status = status;
        this.count = chatUsers.size();
    }

    public ChatMessageResponse(List<ChatInterface> chatInterfaces) {
        for(ChatInterface chatInterface : chatInterfaces) chatUsers.add(ChatUser.of(chatInterface));
        this.status = "LOG";
        this.count = chatUsers.size();
    }

    public ChatMessageResponse(Long memberId, List<Chat> chats) {
        chatUsers.add(ChatUser.of(memberId, chats));
        this.status = "GET";
        this.count = 1;
    }

    public ChatMessageResponse(ChatMessage chatMessage) {
        chatUsers.add(ChatUser.of(chatMessage));
        this.status = "SEND";
        this.count = 1;
    }

    public ChatMessageResponse(String status) {
        this.status = status;
    }

    private ChatUser getChatUserByMemberId(Long memberId) {
        for(ChatUser chatUser : chatUsers) {
            if(chatUser.getMemberId().equals(memberId)) return chatUser;
        }
        ChatUser newChatUser = new ChatUser();
        newChatUser.setMemberId(memberId);
        chatUsers.add(newChatUser);

        return newChatUser;
    }

}
