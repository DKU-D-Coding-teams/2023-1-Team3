package DKUDCoding20231Team3.VISTA.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class SessionRepositrory {

    public static Map<Long, String> sessionMap = new ConcurrentHashMap<>();

    public boolean existByMemberId(Long memberId) {
        return sessionMap.containsKey(memberId);
    }

    public void updateSession(String sessionId, Long memberId) {
        if(existByMemberId(memberId)) sessionMap.remove(memberId);
        sessionMap.put(memberId, sessionId);
        log.info("Websocket SUBSCRIBE : " + sessionId + " / " + memberId);
    }

    public void deleteBySessionId(String sessionId) {
        for(Long memberId : sessionMap.keySet()) {
            if(sessionMap.get(memberId).equals(sessionId)) {
                sessionMap.remove(memberId);
                log.info("Websocket DISCONNECT : " + sessionId + " / " + memberId);
                break;
            }
        }
    }

}