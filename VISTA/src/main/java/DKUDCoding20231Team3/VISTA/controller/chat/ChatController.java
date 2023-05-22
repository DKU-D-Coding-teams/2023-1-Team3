package DKUDCoding20231Team3.VISTA.controller.chat;

import DKUDCoding20231Team3.VISTA.domain.entity.Chat;
import DKUDCoding20231Team3.VISTA.domain.repository.ChatRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.SessionRepositrory;
import DKUDCoding20231Team3.VISTA.dto.message.ChatMessage;
import DKUDCoding20231Team3.VISTA.dto.message.ChatMessageResponse;
import DKUDCoding20231Team3.VISTA.dto.message.FetchRequest;
import DKUDCoding20231Team3.VISTA.util.GsonUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRepositrory sessionRepositrory;
    private final ChatRepository chatRepository;
    private final RedisUtil redisUtil;

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        sessionRepositrory.deleteBySessionId(event.getSessionId());
    }

    @MessageMapping("/fetch")
    public void fetchMessage(FetchRequest fetchRequest, Message<?> message) throws Exception {
        final int FETCH_MESSAGE_SIZE = 5;

        final List<ChatMessage> chatMessages = redisUtil.getListData(fetchRequest.getMemberId().toString(), FETCH_MESSAGE_SIZE);
        final String status = (redisUtil.getListDataLength(fetchRequest.getMemberId().toString()) > 0) ? "RE-FETCH" : "FETCH";

        chatRepository.saveAll(Chat.of(chatMessages));

        for(ChatMessage chatMessage : chatMessages) {
            log.info("Websocket FETCH : " + chatMessage.getMessage());
        }
        messagingTemplate.convertAndSend("/topic/" + fetchRequest.getMemberId(),
                ChatMessageResponse.of(status, chatMessages.size(), chatMessages));
    }

    @MessageMapping("/send")
    public void sendMessage(ChatMessage chatMessage, Message<?> message) throws Exception {
        log.info("Websocket SEND : " + GsonUtil.toJson(chatMessage));

        if(sessionRepositrory.existByMemberId(chatMessage.getRecvMemberId())) {
            chatRepository.save(Chat.of(chatMessage));

            messagingTemplate.convertAndSend("/topic/" + chatMessage.getRecvMemberId(),
                    ChatMessageResponse.of("SEND", 1, chatMessage));

            log.info("Websocket CASE 2 : " + chatMessage.getMessage());
        }
        else {
            redisUtil.setListData(chatMessage);

            log.info("Websocket CASE 1 : " + chatMessage.getMessage());
        }
    }

}