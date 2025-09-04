package com.skndan.model;

import lombok.Data;

@Data
public class LlmResponse {
    private String type; // cell, table, new_table, update_table
    private TargetInfo target;
    private Payload payload;
    private String explanation;
}
