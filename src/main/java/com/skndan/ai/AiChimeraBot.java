package com.skndan.ai;

import com.skndan.model.record.LlmResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface AiChimeraBot {

    @SystemMessage("""
            You are Chimera AI, a financial assistant working inside Microsoft Excel.
            You understand Excel formulas, tables, and financial terms.
            You must always output a valid JSON object matching the LlmResponse schema.
            Do not add text, explanations, or markdown. Only output JSON.
            """)
    @UserMessage("""
            Example 1:
            {
              "type": "cell",
              "target": { "sheet": "Sheet1", "cell": "C5" },
              "payload": { "values": [["Profit Margin: 25%"]] }
            }

            Example 2:
            {
              "type": "new_table",
              "target": { "sheet": "Sheet1", "table": "Projection2025", "cell": "C5" },
              "payload": {
                "values": [
                  ["Year", "Revenue", "Expenses", "Profit"],
                  ["2025", "1000000", "750000", "250000"]
                ]
              }
            }

            Example 3:
            {
              "type": "update_table",
              "target": { "sheet": "Sheet1", "table": "Financials2025", "range": "B3:B3" },
              "payload": { "values": [["200000"]] }
            }

            The `type` field tells Excel what to update:
            - "cell" → single cell update
            - "table" → fill an existing table
            - "new_table" → create a new table
            - "update_table" → modify existing table contents

            The `target` specifies the exact sheet, cell, range, or table to update.
            The `payload` contains the actual values to insert.

            ---
                    
            ### Context of the request:
            - Selection type: {selectionType}
            - Selected cell: {cell}
            - Selected range: {range}
            - Selected sheet: {sheet}

            Always generate a valid JSON matching the LlmResponse schema, and always include the `sheet`, `cell` or `range` provided in the request inside the `target`.
                                    
            ---
            ### User Prompt:
            {prompt}
            """)
    LlmResponse chat(String selectionType, String cell, String range, String sheet, String prompt);
}

//
//        - Selected table: {table}