package com.skndan.model.request;

import lombok.Data;

@Data
public class Content {
    String cell;            // Single cell value (if any)
    String[][] range;       // 2D array for selected range
    String sheet;           // Sheet name
}
