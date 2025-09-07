package com.skndan.resource;

import com.skndan.service.TicketService;
import com.skndan.entity.Paged;
import com.skndan.entity.Theatre;
import com.skndan.entity.Ticket;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

@Path("/api/ticket")
public class TicketResource {

    @Inject
    TicketService ticketService;

    @GET
    @Path("{id}")
    public Ticket getTicket(@PathParam("id") Long id) {
        return ticketService.getTicket(id);
    }

    @GET
    @Path("/filter")
    public Paged<Ticket> getTickets(
            @QueryParam("pageNo") @DefaultValue("0") int pageNo,
            @QueryParam("pageSize") @DefaultValue("25") int pageSize,
            @QueryParam("theatreId") @DefaultValue("0") Long theatreId
    ) {
        return ticketService.getTickets2(pageNo, pageSize, theatreId);
    }

    @GET
    public Paged<Ticket> getTickets2(
            @QueryParam("pageNo") @DefaultValue("0") int pageNo,
            @QueryParam("pageSize") @DefaultValue("25") int pageSize,
            @QueryParam("sortField") @DefaultValue("createdAt") String sortField,
            @QueryParam("sortDir") @DefaultValue("ASC") String sortDir
    ) {
        return ticketService.getTickets(pageNo, pageSize);
    }

    @POST
    public Ticket createTicket(Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @POST
    @Transactional
    @Path("/theatre")
    public Theatre createTheatre(Theatre theatre) {
        theatre.persist();
        return theatre;
    }
}
