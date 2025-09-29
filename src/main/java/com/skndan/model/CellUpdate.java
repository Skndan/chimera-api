package com.skndan.model;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Represents an update to a specific cell in a spreadsheet")
public class CellUpdate {
    @Schema(description = "Row index of the cell", example = "2")
    private int row;

    @Schema(description = "Column index of the cell", example = "3")
    private int col;

    @Schema(description = "New value for the cell", example = "Updated Content")
    private String value;
}
