package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

public record LlmResponse(
        String type,     // "cell" | "table" | "new_table" | "update_table"
        Target target,   // Describes where in Excel to write the output
        Payload payload  // The actual content or values to insert
) {
    @JsonCreator
    public LlmResponse {
    }
}
