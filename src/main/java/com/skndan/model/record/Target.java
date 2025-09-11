package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

public record Target(
        String sheet,
        String cell,   // e.g. "B5"
        String range,  // e.g. "A1:C10"
        String table   // table name if applicable
) {
    @JsonCreator
    public Target {
    }
}
