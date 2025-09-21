package com.skndan.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;

@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class ChatResource {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        broadcast("User " + username + " joined");
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        broadcast(">> " + username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }


//    private static final ObjectMapper MAPPER = new ObjectMapper();
//
//    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
//
//    @Inject
//    ChatService chatService; // your LLM orchestrator

//    @OnMessage
//    public void onMessage(String message, @PathParam("chatRoomId") String chatRoomId) {
//        try {
//            // Parse incoming JSON from taskpane
//            LlmRequest request = MAPPER.readValue(message, LlmRequest.class);
//
//            // When complete, send final structured LlmResponse
//            LlmResponse finalResponse = chatService.chat(chatRoomId, request);
//            session.getBasicRemote().sendText(MAPPER.writeValueAsString(finalResponse));
//
//        } catch (Exception e) {
//            try {
//                session.getBasicRemote().sendText("{\"error\":\"" + e.getMessage() + "\"}");
//            } catch (IOException ignored) {}
//        }
//    }
}