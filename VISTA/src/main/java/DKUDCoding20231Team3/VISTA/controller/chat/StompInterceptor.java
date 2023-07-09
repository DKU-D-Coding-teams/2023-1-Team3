package DKUDCoding20231Team3.VISTA.controller.chat;

import DKUDCoding20231Team3.VISTA.domain.repository.SessionRepositrory;
import DKUDCoding20231Team3.VISTA.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {
    private final SessionRepositrory sessionRepositrory;
    private final JwtUtil jwtUtil;


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
            final String accessToken = headerAccessor.getFirstNativeHeader("accessToken");
            final String memberId = headerAccessor.getFirstNativeHeader("memberId");

            System.out.println("StompInterceptor presend() - accessToken: " + accessToken);

            if (!jwtUtil.validateAccessToken(accessToken)) {
                System.out.println("hello, world");

                StompHeaderAccessor headerAccessor2 = StompHeaderAccessor.create(StompCommand.MESSAGE);
                headerAccessor2.setSessionId(headerAccessor.getSessionId());
                headerAccessor2.setSubscriptionId(headerAccessor.getSubscriptionId());

                headerAccessor2.setMessage("FULL");
                channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor2.getMessageHeaders()));
            }
//            else {
//
//                StompHeaderAccessor headerAccessor2 = StompHeaderAccessor.create(StompCommand.MESSAGE);
//                headerAccessor2.setSessionId(headerAccessor.getSessionId());
//                headerAccessor2.setSubscriptionId(headerAccessor.getSubscriptionId());
//
//                headerAccessor2.setMessage("FULL");
//                channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor2.getMessageHeaders()));
//            }
        }

        return message;
    }

}
