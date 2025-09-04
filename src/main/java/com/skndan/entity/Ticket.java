package com.skndan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
public class Ticket extends BaseEntity {
    public String title;
    public String description;

    @ManyToOne
    public Theatre theatre;
}