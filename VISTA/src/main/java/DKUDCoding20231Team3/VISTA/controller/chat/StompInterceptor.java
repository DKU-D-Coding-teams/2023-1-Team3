package DKUDCoding20231Team3.VISTA.controller.chat;

import DKUDCoding20231Team3.VISTA.domain.repository.SessionRepositrory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private final SessionRepositrory sessionRepositrory;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            log.info("Websocket CONNECT");

        } else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            final String sessionId = headerAccessor.getSessionId();
            final Long memberId = Long.parseLong(Objects.requireNonNull(headerAccessor.getFirstNativeHeader("memberId")));
            sessionRepositrory.updateSession(sessionId, memberId);
        } else if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            final String AccessToken = headerAccessor.getFirstNativeHeader("access_token");

            

            /*
                AT_HEADER = "access_token";
                RT_HEADER = "refresh_token";

                1. AT 유효성 검사 성공 -> 아무것도 안해
                2. AT 유효성 검사 실패 ->

             */

        }

        return message;
    }

}
