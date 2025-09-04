package com.skndan.model;

import lombok.Data;

import java.util.List;

@Data
public class Payload {
    private String value; // For single cell
    private String[][] values; // For full table
    private List<CellUpdate> updates; // For updating existing table
}
