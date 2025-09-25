package com.skndan.chat;

import io.quarkus.websockets.next.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@WebSocket(path = "/addin-chat/{chatRoomId}")
@ApplicationScoped
public class ChatWebSocket {

    // Declare the type of messages that can be sent and received
    public enum MessageType {USER_JOINED, USER_LEFT, CHAT_MESSAGE}
    public record ChatMessage(MessageType type, String from, String message) {
    }

    @Inject
    WebSocketConnection connection;

    @OnOpen(broadcast = true)
    public ChatMessage onOpen() {
        return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("chatRoomId"), null);
    }

    @OnClose
    public void onClose() {
        ChatMessage departure = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("chatRoomId"), null);
        connection.broadcast().sendTextAndAwait(departure);
    }

    @OnTextMessage(broadcast = true)
    public ChatMessage onMessage(ChatMessage message) {
        ChatMessage departure = new ChatMessage(MessageType.CHAT_MESSAGE, connection.pathParam("chatRoomId"), message.message);

        connection.broadcast().sendTextAndAwait(departure);
        return message;
    }

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