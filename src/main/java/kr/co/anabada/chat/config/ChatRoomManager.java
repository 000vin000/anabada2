package kr.co.anabada.chat.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

public class ChatRoomManager {
	// 세션관리
    private static final Map<Integer, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public static void addSession(Integer roomNo, WebSocketSession session) {
        roomSessions.computeIfAbsent(roomNo, k -> new CopyOnWriteArrayList<>()).add(session);
    }

    public static void removeSession(Integer roomNo, WebSocketSession session) {
        List<WebSocketSession> sessions = roomSessions.get(roomNo);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public static List<WebSocketSession> getSessions(Integer roomNo) {
        return roomSessions.getOrDefault(roomNo, Collections.emptyList());
    }
}

