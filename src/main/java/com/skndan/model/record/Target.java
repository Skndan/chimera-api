package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

public record Target(
        String cell,   // e.g. "B5" (can be empty if using range/table)
        String range,  // e.g. "A1:C10" (optional, depends on type)
        String sheet   // Name of the sheet (e.g. "Sheet1")
) {
    @JsonCreator
    public Target {
    }
}
