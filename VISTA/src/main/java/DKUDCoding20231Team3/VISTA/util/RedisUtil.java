package DKUDCoding20231Team3.VISTA.util;

import DKUDCoding20231Team3.VISTA.dto.message.ChatMessage;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public List<ChatMessage> getListData(String key, int size) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> jsonListChatMessages = Objects.requireNonNull(listOperations.leftPop(key, size));
        List<ChatMessage> chatMessages = new ArrayList<>();
        for(String jsonChatMessage : jsonListChatMessages) {
            chatMessages.add(GsonUtil.fromJson(jsonChatMessage, ChatMessage.class));
        }

        return chatMessages;
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void setListData(ChatMessage chatMessage) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(chatMessage.getRecvMemberId().toString(), Objects.requireNonNull(GsonUtil.toJson(chatMessage)));
    }

    public Long getListDataLength(String key) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.size(key);
    }

}
