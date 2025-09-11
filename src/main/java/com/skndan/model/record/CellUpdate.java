package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;

public record CellUpdate(
        String formula,              // e.g. "=SUM(A1:A10)" OR empty if value-only
        String value                 // final value to write in cell (string)
) {
    @JsonCreator
    public CellUpdate {
    }
}
