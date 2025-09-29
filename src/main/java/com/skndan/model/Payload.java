package com.skndan.model;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Schema(description = "Represents the payload of an LLM response")
public class Payload {
    @Schema(description = "Value for a single cell", example = "Sample Value")
    private String value;

    @Schema(description = "2D array of values for a full table",
            example = "[['Header1', 'Header2'], ['Value1', 'Value2']]")
    private String[][] values;

    @Schema(description = "List of cell updates for modifying an existing table")
    private List<CellUpdate> updates;
}
