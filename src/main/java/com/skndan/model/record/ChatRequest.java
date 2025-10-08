package com.skndan.model.record;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


/**
 * Represents a chat request with details about the conversation context and Excel selection.
 *
 * @param roomName the name of the chat room
 * @param prompt the user's input message
 * @param selectionType the type of Excel selection ("cell", "range", or "table")
 * @param cell the specific cell reference (e.g., "B5")
 * @param range the cell range reference (e.g., "B5:D10")
 * @param sheet the name of the Excel sheet
 */
@Schema(description = "Represents a chat request with details about the conversation context and Excel selection")
public record ChatRequest(
    @Schema(description = "Name of the chat room", example = "Financial Analysis")
    String roomName,
    
    @Schema(description = "User's input message", example = "Analyze the sales data")
    String prompt,
    
    @Schema(description = "Type of Excel selection", example = "cell", type = SchemaType.STRING, enumeration = {"cell", "range", "table"})
    String selectionType,
    
    @Schema(description = "Specific cell reference", example = "B5")
    String cell,
    
    @Schema(description = "Cell range reference", example = "B5:D10")
    String range,
    
    @Schema(description = "Name of the Excel sheet", example = "Sales 2025")
    String sheet
) {
    @JsonCreator
    public ChatRequest {
    }
}
