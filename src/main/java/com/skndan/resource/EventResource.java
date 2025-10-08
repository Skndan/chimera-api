package com.skndan.resource;

import com.skndan.service.EventService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * REST resource for handling real-time event streaming using Server-Sent Events (SSE).
 * Provides endpoints for subscribing to event streams and testing event notifications.
 */
@Path("/events")
@ApplicationScoped
@Tag(name = "Events", description = "Real-time event streaming endpoints using Server-Sent Events")
public class EventResource {

    @Inject
    EventService eventService;

    @Inject
    ManagedExecutor executor;

    /**
     * Simple response record for chat operations.
     */
    @Schema(description = "Response model for chat operations")
    public record ChatResponse(
        @Schema(description = "The reply message from the server", example = "Processing")
        String reply
    ) {
    }

    /**
     * Establishes a Server-Sent Events (SSE) stream for a specific chat room.
     * Clients can subscribe to this endpoint to receive real-time messages.
     *
     * @param roomId the unique identifier for the chat room
     * @return a stream of events in SSE format
     */
    @GET
    @Path("/room/{roomId}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Operation(
        summary = "Subscribe to chat room events",
        description = "Establishes a Server-Sent Events (SSE) stream for real-time message delivery"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successful SSE stream connection",
        content = @Content(mediaType = MediaType.SERVER_SENT_EVENTS)
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid room ID provided"
    )
    public Multi<String> stream(
        @Parameter(description = "Unique identifier for the chat room", required = true)
        @PathParam("roomId") String roomId) {
        return eventService.register(roomId);
    }

    /**
     * Test endpoint that simulates sending messages to a chat room.
     * Sends two test messages with delays to demonstrate real-time functionality.
     *
     * @param roomId the unique identifier for the chat room
     * @return immediate response indicating processing has started
     */
    @GET
    @Path("/room-test/{roomId}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(
        summary = "Test event streaming functionality",
        description = "Simulates sending test messages to a chat room with delays to demonstrate real-time capabilities"
    )
    @APIResponse(
        responseCode = "202",
        description = "Test messages processing started",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ChatResponse.class))
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid room ID provided"
    )
    public Response testStream(
        @Parameter(description = "Unique identifier for the chat room", required = true)
        @PathParam("roomId") String roomId) {

        // Fire async processing
        executor.execute(() -> {
            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                eventService.notify(roomId, "Hello from backend!1");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                eventService.notify(roomId, "Hello from backend!2");
            } catch (Exception e) {
                eventService.notify(roomId, "Error: " + e.getMessage());
            }
        });

        return Response.accepted()
                .entity(new ChatResponse("Processing"))
                .build();
    }
}