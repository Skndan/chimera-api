package com.skndan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Settings extends BaseEntity {
    private UUID profileId;
    private String modelName;
    private String instructions;
}