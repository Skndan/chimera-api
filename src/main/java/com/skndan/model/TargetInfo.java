package com.skndan.model;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Represents the target location for an LLM response in a spreadsheet")
public class TargetInfo {
    @Schema(description = "Name of the target sheet", example = "Sheet1")
    private String sheetName;

    @Schema(description = "Specific cell reference for single cell operations", example = "B5")
    private String cell;

    @Schema(description = "Cell range for table operations", example = "A1:D10")
    private String range;

    @Schema(description = "Unique identifier for a specific table", example = "table_123")
    private String tableId;
}
