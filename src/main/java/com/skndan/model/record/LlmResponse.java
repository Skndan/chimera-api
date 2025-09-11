package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

public record LlmResponse(
        String type,                 // "cell" | "table" | "new_table" | "update_table"
        Target target,
        Payload payload
) {
    @JsonCreator
    public LlmResponse {
    }
}
