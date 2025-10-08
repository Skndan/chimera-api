package com.skndan.model.request;

import lombok.Data;

@Data
public class Output {
    String cell;   // Target cell (nullable)
    String range;  // Target range (nullable)
    String sheet;  // Target sheet
}
