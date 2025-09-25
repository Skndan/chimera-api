package com.skndan.resource;
//
//import com.skndan.entity.ChatRoom;
//import com.skndan.entity.Paged;
//import com.skndan.repo.ChatRoomRepo;
//import jakarta.inject.Inject;
//import jakarta.transaction.Transactional;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.Context;
//import jakarta.ws.rs.core.SecurityContext;
//import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
//
//@Path("/chat-room")
public class ChatRoomResource {
//
//    //
//
//    @Inject
//    ChatRoomRepo repo;
//
//    // create new room
//    @POST
//    @Transactional
//    public ChatRoom createChatRoom(@Context SecurityContext context, @RequestBody ChatRoom chatRoom) {
//
//        if(chatRoom.getId() != null) {
//            throw new WebApplicationException("Id was invalidly set on request.", 422);
//        }
//
//        String userId = ((org.eclipse.microprofile.jwt.JsonWebToken) context.getUserPrincipal())
//                .getSubject();
//
//        chatRoom.setUserId(userId);
//        chatRoom.persist();
//
//        return chatRoom;
//    }
//
//    // get all room by user
//    @GET
//    public Paged<ChatRoom> getAllChatRoom(@Context SecurityContext context,
//                                          @QueryParam("pageNo") @DefaultValue("0") int pageNo,
//                                          @QueryParam("pageSize") @DefaultValue("25") int pageSize) {
//
//        String userId = ((org.eclipse.microprofile.jwt.JsonWebToken) context.getUserPrincipal())
//                .getSubject();
//
//        return repo.findAllPagedById(pageNo, pageSize, userId);
//    }
//
}