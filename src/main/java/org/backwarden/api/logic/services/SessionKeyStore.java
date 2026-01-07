package org.backwarden.api.logic.services;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionKeyStore {

    private final Map<String, SecretKey> store = new ConcurrentHashMap<>();

    public void put(String sessionId, SecretKey key) {
        store.put(sessionId, key);
    }

    public SecretKey get(String sessionId) {
        return store.get(sessionId);
    }

    public void remove(String sessionId) {
        store.remove(sessionId);
    }
}
