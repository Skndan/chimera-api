package com.skndan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatRoom extends BaseEntity {

    @Column(nullable = false)
    public String userId;

    @Column(nullable = false)
    public String name;

}