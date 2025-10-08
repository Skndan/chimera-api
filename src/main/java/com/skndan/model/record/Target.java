package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Represents the target location in an Excel sheet for LLM response output.
 *
 * @param cell the specific cell reference (e.g., "B5"), can be empty if using range/table
 * @param range the cell range reference (e.g., "A1:C10"), optional depending on type
 * @param sheet the name of the Excel sheet (e.g., "Sheet1")
 */
@Schema(description = "Represents the target location in an Excel sheet for LLM response output")
public record Target(
        @Schema(description = "Specific cell reference (can be empty if using range/table)",
                example = "B5")
        String cell,
        
        @Schema(description = "Cell range reference (optional, depends on response type)",
                example = "A1:C10")
        String range,
        
        @Schema(description = "Name of the Excel sheet", example = "Sheet1")
        String sheet
) {
    @JsonCreator
    public Target {
    }
}
