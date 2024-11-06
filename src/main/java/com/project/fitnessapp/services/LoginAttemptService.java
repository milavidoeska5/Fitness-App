package com.project.fitnessapp.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 3;
    private final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> lockoutCache = new HashMap<>();

    public void recordFailedAttempt(HttpServletRequest request) {
        String sessionId = getSessionId(request);

        if (isSessionLockedOut(sessionId)) {
            return;
        }

        attemptsCache.put(sessionId, attemptsCache.getOrDefault(sessionId, 0) + 1);
    }

    public boolean isSessionLockedOut(String sessionId) {
        Long lockoutTime = lockoutCache.get(sessionId);
        if (lockoutTime != null) {
            if (System.currentTimeMillis() - lockoutTime < LOCK_TIME_DURATION) {
                return true;
            } else {
                unlockSession(sessionId);
                return false;
            }
        }
        return false;
    }

    public void unlockSession(String sessionId) {
        attemptsCache.remove(sessionId);
        lockoutCache.remove(sessionId);
    }

    public void checkAndLockSession(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        if (attemptsCache.getOrDefault(sessionId, 0) >= MAX_ATTEMPT) {
            lockoutCache.put(sessionId, System.currentTimeMillis());
            attemptsCache.remove(sessionId);
        }
    }

    private String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }
}
