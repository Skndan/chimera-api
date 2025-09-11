package com.skndan.resource;

import com.skndan.model.CellUpdate;
import com.skndan.model.LlmResponseModel;
import com.skndan.model.Payload;
import com.skndan.model.TargetInfo;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;

@Path("/excel")
public class ExcelResource {

    @GET
    @Path("/cell")
    public Response mockCell() {
        LlmResponseModel res = new LlmResponseModel();
        res.setType("cell");

        TargetInfo target = new TargetInfo();
        target.setSheetName("Sheet1");
        target.setCell("B5");
        res.setTarget(target);

        Payload payload = new Payload();
        payload.setValue("240B");
        res.setPayload(payload);

        res.setExplanation("Inserted Tesla revenue for 2025.");
        return Response.ok(res).build();
    }

    @GET
    @Path("/table")
    public Response mockTable() {
        LlmResponseModel res = new LlmResponseModel();
        res.setType("table");

        TargetInfo target = new TargetInfo();
        target.setSheetName("Sheet1");
        target.setRange("A5:C10");
        res.setTarget(target);

        Payload payload = new Payload();
        payload.setValues(new String[][]{
                {"Year", "Revenue", "Profit"},
                {"2023", "96.8B", "14.2B"},
                {"2024", "110B", "16.1B"},
                {"2025", "125B", "18.5B"}
        });
        res.setPayload(payload);

        res.setExplanation("Generated Tesla revenue/profit table.");
        return Response.ok(res).build();
    }

    @GET
    @Path("/new-table")
    public Response mockNewTable() {
        LlmResponseModel res = new LlmResponseModel();
        res.setType("new_table");

        TargetInfo target = new TargetInfo();
        target.setSheetName("Sheet2");
        target.setRange("A1:D4");
        target.setTableId("CompanyFinancials");
        res.setTarget(target);

        Payload payload = new Payload();
        payload.setValues(new String[][]{
                {"Company", "Year", "Revenue", "Profit"},
                {"Tesla", "2025", "125B", "18.5B"},
                {"Apple", "2025", "420B", "100B"}
        });
        res.setPayload(payload);

        res.setExplanation("Created new table for multiple companies.");
        return Response.ok(res).build();
    }

    @GET
    @Path("/update-table")
    public Response mockUpdateTable() {
        LlmResponseModel res = new LlmResponseModel();
        res.setType("update_table");

        TargetInfo target = new TargetInfo();
        target.setSheetName("Sheet1");
        target.setTableId("Financials2025");
        res.setTarget(target);

        Payload payload = new Payload();
        payload.setUpdates(Arrays.asList(
                createUpdate(6, 2, "250B"),
                createUpdate(6, 3, "17B")
        ));
        res.setPayload(payload);

        res.setExplanation("Updated 2025 revenue and profit values.");
        return Response.ok(res).build();
    }

    private CellUpdate createUpdate(int row, int col, String value) {
        CellUpdate update = new CellUpdate();
        update.setRow(row);
        update.setCol(col);
        update.setValue(value);
        return update;
    }

}
