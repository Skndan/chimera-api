package com.skndan.repo;

import com.skndan.entity.Paged;
import io.quarkus.panache.common.Page;
import com.skndan.entity.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TicketRepo extends BaseRepo<Ticket, Long> implements PanacheRepository<Ticket> {

    public Paged<Ticket> findAllPaged(int page, int size) {

        PanacheQuery<Ticket> tickets = Ticket.find("active", true);
        long total = tickets.count();

        List<Ticket> ticketPaged = tickets.page(Page.of(page, size)).list();

        return new Paged<>(ticketPaged, total, page, size);
    }

    public Paged<Ticket> findAllPaged2(int page, int size, Long theatreId) {
        return findPaged("active = ?1 and theatre.id = ?2", page, size, true, theatreId);
    }
}