package com.skndan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * Represents a ticket in the system.
 * Stores information about a ticket's title, description, and associated theatre.
 */
@Entity
public class Ticket extends BaseEntity {
    /**
     * The title of the ticket.
     * Provides a brief description or identifier for the ticket.
     */
    public String title;
    /**
     * A detailed description of the ticket.
     * Offers more comprehensive information about the ticket's purpose or details.
     */
    public String description;

    /**
     * The theatre associated with this ticket.
     * Establishes a many-to-one relationship with the Theatre entity.
     */
    @ManyToOne
    public Theatre theatre;
}