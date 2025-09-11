package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public record Payload(
        String text,             // optional human-readable explanation text (nullable)
        CellUpdate cell,         // for single-cell updates (nullable)
        String[][] values,       // for table / new_table -> 2D array (nullable)
        CellUpdate[] updates     // for update_table -> array of updates (nullable)
) {
    @JsonCreator
    public Payload {
    }
}
