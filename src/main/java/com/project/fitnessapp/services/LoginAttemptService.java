package com.project.fitnessapp.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 3;
    private final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> lockoutCache = new HashMap<>();

    public void recordFailedAttempt(String username) {
        if (isUserLockedOut(username)) {
            return;
        }
        attemptsCache.put(username, attemptsCache.getOrDefault(username, 0) + 1);
    }

    public boolean isUserLockedOut(String username) {
        if (lockoutCache.containsKey(username)) {
            long lockoutTime = lockoutCache.get(username);
            if (System.currentTimeMillis() - lockoutTime < LOCK_TIME_DURATION) {
                return true;
            } else {
                unlockUser(username);
            }
        }
        return false;
    }

    public void unlockUser(String username) {
        attemptsCache.remove(username);
        lockoutCache.remove(username);
    }

    public void checkAndLockUser(String username) {
        if (attemptsCache.getOrDefault(username, 0) >= MAX_ATTEMPT) {
            lockoutCache.put(username, System.currentTimeMillis());
            attemptsCache.remove(username);
        }
    }
}
