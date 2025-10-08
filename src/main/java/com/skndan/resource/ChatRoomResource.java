package com.skndan.resource;

import com.skndan.entity.ChatRoom;
import com.skndan.entity.Paged;
import com.skndan.repo.ChatRoomRepo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * REST endpoint for managing chat rooms.
 * Provides functionality to create and retrieve chat rooms for authenticated users.
 */
@Path("/chat-room")
@Tag(name = "Chat Rooms", description = "Manage chat room operations")
public class ChatRoomResource {

    @Inject
    ChatRoomRepo repo;

    // create new room
    /**
     * Creates a new chat room for the authenticated user.
     * Validates that no ID is pre-set and associates the room with the user's identity.
     *
     * @param context security context to extract user information
     * @param chatRoom the chat room to be created
     * @return the newly created chat room
     * @throws WebApplicationException if an invalid ID is provided
     */
    @POST
    @Transactional
    @Operation(
        summary = "Create a new chat room",
        description = "Creates a chat room for the authenticated user"
    )
    @APIResponse(
        responseCode = "200",
        description = "Chat room successfully created",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ChatRoom.class))
    )
    @APIResponse(
        responseCode = "422",
        description = "Invalid chat room data"
    )
    public ChatRoom createChatRoom(
        @Context SecurityContext context,
        @RequestBody(
            description = "Chat room details",
            content = @Content(schema = @Schema(implementation = ChatRoom.class))
        ) ChatRoom chatRoom
    ) {

        if(chatRoom.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        String userId = ((org.eclipse.microprofile.jwt.JsonWebToken) context.getUserPrincipal())
                .getSubject();

        chatRoom.setUserId(userId);
        chatRoom.persist();

        return chatRoom;
    }

    // get all room by user
    /**
     * Retrieves all chat rooms for the authenticated user with pagination support.
     *
     * @param context security context to extract user information
     * @param pageNo the page number for pagination (default: 0)
     * @param pageSize number of items per page (default: 25)
     * @return a paginated list of chat rooms belonging to the user
     */
    @GET
    @Operation(
        summary = "Retrieve chat rooms",
        description = "Retrieves paginated list of chat rooms for the authenticated user"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully retrieved chat rooms",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Paged.class))
    )
    public Paged<ChatRoom> getAllChatRoom(
        @Context SecurityContext context,
        @Parameter(description = "Page number", example = "0")
        @QueryParam("pageNo") @DefaultValue("0") int pageNo,
        @Parameter(description = "Number of items per page", example = "25")
        @QueryParam("pageSize") @DefaultValue("25") int pageSize
    ) {

        String userId = ((org.eclipse.microprofile.jwt.JsonWebToken) context.getUserPrincipal())
                .getSubject();

        return repo.findAllPagedById(pageNo, pageSize, userId);
    }

}