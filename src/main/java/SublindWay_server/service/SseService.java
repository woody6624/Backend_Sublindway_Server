package SublindWay_server.service;

import SublindWay_server.dto.SendWebData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service

public class SseService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, SendWebData> lastKnownLocations = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        SendWebData initialLocation = lastKnownLocations.get(userId);
        if (initialLocation != null) {
            sendEventToUser(userId, initialLocation);
        }

        return emitter;
    }

    public void sendEventToUser(String userId, SendWebData sendWebData) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(sendWebData, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    public void updateLastKnownLocation(String userId, SendWebData sendWebData) {
        lastKnownLocations.put(userId, sendWebData);
    }

}
