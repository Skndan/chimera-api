package com.skndan.model.request;

import lombok.Data;

import java.util.Arrays;

@Data
public class LlmRequest {

    String role;
    String prompt;  // The user’s actual query
    Content content;
    Output output;
    long roomId;

    @Override
    public String toString() {

        return "### User Prompt:\n" +
                prompt + "\n\n" +
                "### Excel Context:\n" +
                "Sheet: " + content.getSheet() + "\n" +
                "Cell: " + content.getCell() + "\n" +
                "Range: " + Arrays.deepToString(content.getRange()) + "\n\n" +
                "### Desired Output:\n" +
                "Sheet: " + output.getSheet() + "\n" +
                "Cell: " + output.getCell() + "\n" +
                "Range: " + output.getRange() + "\n";
    }
}


//
///**
// * Represents the request you send from Taskpane → Quarkus API → LLM.
// */
//public record LlmRequest(
//        String prompt,  // The user’s actual query
//        Content content,
//        Output output
//) {
//    /**
//     * Input context from Excel (cell, range, sheet).
//     */
//    public record Content(
//            String cell,               // Single cell value (if any)
//            List<List<String>> range,  // 2D array for selected range
//            String sheet               // Sheet name
//    ) {}
//
//    /**
//     * Desired output location in Excel.
//     */
//    public record Output(
//            String cell,   // Target cell (nullable)
//            String range,  // Target range (nullable)
//            String sheet   // Target sheet
//    ) {}
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("### User Prompt:\n")
//                .append(prompt).append("\n\n");
//
//        sb.append("### Excel Context:\n")
//                .append("Sheet: ").append(content.sheet()).append("\n")
//                .append("Cell: ").append(content.cell()).append("\n")
//                .append("Range: ").append(content.range()).append("\n\n");
//
//        sb.append("### Desired Output:\n")
//                .append("Sheet: ").append(output.sheet()).append("\n")
//                .append("Cell: ").append(output.cell()).append("\n")
//                .append("Range: ").append(output.range()).append("\n");
//
//        return sb.toString();
//    }
//}
