package SublindWay_server.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InMemoryRateLimitingService {

    private static final int MAX_REQUESTS = 5; // 예시: 최대 5회
    private static final Duration TIME_WINDOW = Duration.ofMinutes(1); // 예시: 1분

    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    public boolean isRateLimitExceeded(String kakaoId) {
        RateLimitInfo rateLimitInfo = rateLimitMap.computeIfAbsent(kakaoId, k -> new RateLimitInfo());
        synchronized (rateLimitInfo) {
            if (rateLimitInfo.getTimestamp().isBefore(Instant.now().minus(TIME_WINDOW))) {
                rateLimitInfo.reset();
            }
            rateLimitInfo.increment();
            return rateLimitInfo.getRequestCount() > MAX_REQUESTS;
        }
    }

    private static class RateLimitInfo {
        private AtomicInteger requestCount;
        private Instant timestamp;

        public RateLimitInfo() {
            this.requestCount = new AtomicInteger(0);
            this.timestamp = Instant.now();
        }

        public void increment() {
            requestCount.incrementAndGet();
        }

        public int getRequestCount() {
            return requestCount.get();
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void reset() {
            requestCount.set(0);
            timestamp = Instant.now();
        }
    }
}
