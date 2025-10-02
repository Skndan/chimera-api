package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Represents a response from a Language Learning Model (LLM) with Excel-specific output details.
 *
 * @param type the type of response ("cell", "table", "new_table", or "update_table")
 * @param target describes the Excel location for writing the output
 * @param payload the actual content or values to be inserted
 */
@Schema(description = "Represents a response from a Language Learning Model (LLM) with Excel-specific output details")
public record  LlmResponse(
        @Schema(description = "Type of response", examples = "cell", type = SchemaType.STRING,
                enumeration = {"cell", "table", "new_table", "update_table"})
        String type,
        
        @Schema(description = "Describes where in Excel to write the output")
        Target target,
        
        @Schema(description = "The actual content or values to insert")
        Payload payload
) {
    @JsonCreator
    public LlmResponse {
    }
}
