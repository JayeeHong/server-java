package kr.hhplus.be.server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DuplicateRequestInterceptor implements HandlerInterceptor {

    // 요청 키 저장소 (메모리 버전)
    private static final Map<String, Long> requestCache = new ConcurrentHashMap<>();

    // 중복 허용 시간 (예: 3초)
    private static final long TIMEOUT_MILLIS = 3000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String key = generateKey(request);
        Long previousTime = requestCache.get(key);
        long currentTime = System.currentTimeMillis();

        if (previousTime != null && (currentTime - previousTime) < TIMEOUT_MILLIS) {
            log.warn("중복 요청 차단 key={}", key);
            response.sendError(HttpServletResponse.SC_CONFLICT, "중복 요청입니다."); // 409 Conflict
            return false;
        }

        requestCache.put(key, currentTime);
        return true;
    }

    private String generateKey(HttpServletRequest request) {
        // 사용자ID + URI + (중요한 파라미터)
        String userId = request.getHeader("X-USER-ID"); // 예를 들어 사용자 식별 헤더
        if (userId == null) {
            userId = "anonymous";
        }
        String uri = request.getRequestURI();
        String queryString = request.getQueryString(); // 중요 파라미터

        return userId + ":" + uri + "?" + queryString;
    }
}
