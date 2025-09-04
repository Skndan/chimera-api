package com.skndan.model;

import lombok.Data;

@Data
public class CellUpdate {
    private int row;
    private int col;
    private String value;
}
