package com.skndan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket extends BaseEntity {
    public String title;
    public String description;

    @ManyToOne
    public Theatre theatre;
}