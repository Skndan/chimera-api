package com.skndan;

import com.skndan.entity.Paged;
import com.skndan.entity.Ticket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TicketService {

    @Inject
    TicketRepo repo;

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        repo.persist(ticket);
        return ticket;
    }

    public void deleteTicket(Long id) {
        Ticket ticket = repo.findById(id);
        ticket.delete();
    }

    public Ticket getTicket(Long id) {
        return repo.findById(id);
    }

    public Paged<Ticket> getTickets(int page, int size) {
        return repo.findAllPaged(page, size);
    }

    public Paged<Ticket> getTickets2(int page, int size, Long id) {
        return repo.findAllPaged2(page, size, id);
    }


}