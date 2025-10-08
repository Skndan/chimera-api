package com.skndan.model;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;

@Data
@Schema(description = "Represents a response from a Language Learning Model (LLM)")
public class LlmResponseModel {
    @Schema(
        description = "Type of response",
        example = "cell",
        type = SchemaType.STRING,
        enumeration = {"cell", "table", "new_table", "update_table"}
    )
    private String type;

    @Schema(description = "Target information for the response")
    private TargetInfo target;

    @Schema(description = "Payload containing the response data")
    private Payload payload;

    @Schema(description = "Explanation or additional context for the response", example = "Generated financial projection")
    private String explanation;
}
