package DKUDCoding20231Team3.VISTA.domain.entity;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.message.ChatMessage;
import DKUDCoding20231Team3.VISTA.dto.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CHAT_TABLE")
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long chatId;

    private Long sendMemberId;

    private Long recvMemberId;

    private LocalDateTime timeStamp;

    private String message;

    public static Chat of(ChatMessage chatMessage) {
        return Chat.builder()
                .sendMemberId(chatMessage.getSendMemberId())
                .recvMemberId(chatMessage.getRecvMemberId())
                .timeStamp(chatMessage.getTimeStamp())
                .message(chatMessage.getMessage())
                .build();
    }

    public static List<Chat> of(List<ChatMessage> chatMessages) {
        List<Chat> chats = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessages) chats.add(Chat.of(chatMessage));
        return chats;
    }
}
