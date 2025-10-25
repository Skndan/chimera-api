package com.skndan.resource;

import com.skndan.model.record.LlmResponse;
import com.skndan.model.request.LlmRequest;
import com.skndan.service.ChatService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * REST endpoint for AI-powered chat interactions.
 * Provides conversational AI capabilities for chat rooms.
 */
@Path("/ai")
@Tag(name = "AI Chat", description = "AI-powered chat interactions")
public class AiResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    ChatService chatService;

    /**
     * Simple response record for review operations.
     */
    record Review(String review) {
    }

    /**
     * Send a message to the AI chat service for processing.
     * Requires authentication and returns AI-generated responses.
     *
     * @param req the chat request containing room ID and message content
     * @return AI-generated response based on the conversation context
     */
    @POST
    @Path("/chat")
    @Authenticated
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Send a message to the AI chat service",
        description = "Process a chat request and generate an AI response"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successful AI chat response",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = LlmResponse.class))
    )
    @APIResponse(
        responseCode = "401",
        description = "Unauthorized access"
    )
    public LlmResponse chat(
        @RequestBody(
            description = "Chat request details",
            content = @Content(schema = @Schema(implementation = LlmRequest.class))
        ) LlmRequest req
    ) {
        System.out.println("Chat request details: "+req);
        String uid = jwt.getSubject();
        return chatService.chat(req.getRoomId(), req, uid);
    }

}
