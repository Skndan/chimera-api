package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public record Payload(
        String response,    // Optional explanation ("Generated 2025 financial projections.")
        String[][] values   // 2D array of data (rows × columns)
) {
    @JsonCreator
    public Payload {
    }
}
