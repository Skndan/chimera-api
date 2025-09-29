package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Represents the payload of an LLM response, containing an optional explanation and data values.
 *
 * @param response an optional textual explanation of the generated content
 * @param values a 2D array of data representing rows and columns of content
 */
@Schema(description = "Represents the payload of an LLM response, containing an optional explanation and data values")
public record Payload(
        @Schema(description = "Optional explanation of the generated content",
                example = "Generated 2025 financial projections")
        String response,
        
        @Schema(description = "2D array of data representing rows and columns of content")
        String[][] values
) {
    @JsonCreator
    public Payload {
    }
}
