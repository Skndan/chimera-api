package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;


public record ChatRequest(String roomName,
                          String prompt,
                          String selectionType, // "cell" | "range" | "table"
                          String cell,          // e.g. "B5" (if selectionType == cell)
                          String range,         // e.g. "B5:D10" (if selectionType == range)
                          String sheet
) {
    @JsonCreator
    public ChatRequest {
    }
}
