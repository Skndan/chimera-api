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
            Think step by step before answering:
            1. What is the user asking for? (cell value, new table, update table, or full table?)
            2. What is the currently selected sheet, cell, or range?
            3. If a table is being created, where should it start?
            4. If a table is being updated, which range or cells are affected?
            5. Based on that, decide the correct `type` value.
            6. Fill the `target` with the sheet, cell, range, or table name.
            7. Generate the correct `payload.values` with rows/columns.

            ---

            ### Context of the request:
            - Selection type: {selectionType}
            - Selected cell: {cell}
            - Selected range: {range}
            - Selected sheet: {sheet}

            ---

            ### Output format:
            - Always valid JSON (LlmResponse)
            - Must include: `type`, `target`, and `payload`

            ---

            ### Examples:

            **Example 1 (cell update)**  
            User asked: "What is the profit margin if revenue is 1000 and profit is 250?"  
            {
              "type": "cell",
              "target": { "sheet": "Sheet1", "cell": "C5" },
              "payload": { "values": [["Profit Margin: 25%"]] }
            }

            **Example 2 (new table)**  
            User asked: "Create a 2025 financial projection table with revenue, expenses, and profit."  
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

            **Example 3 (update table)**  
            User asked: "Update the expenses for Q2 in Financials2025 table to 200000."  
            {
              "type": "update_table",
              "target": { "sheet": "Sheet1", "table": "Financials2025", "range": "B3:B3" },
              "payload": { "values": [["200000"]] }
            }

            ---

            ### Now process the user prompt:
            {prompt}
            """)
    LlmResponse chat(String selectionType, String cell, String range, String sheet, String prompt);
}

//
//        - Selected table: {table}