package com.skndan.model;

import lombok.Data;

@Data
public class TargetInfo {
    private String sheetName;
    private String cell;   // For single cell
    private String range;  // For tables
    private String tableId;
}
