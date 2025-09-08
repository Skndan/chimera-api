package com.skndan.resource;
import com.skndan.ai.TriageService;
import com.skndan.model.record.TriagedReview;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/review")
public class AiResource {

    @Inject
    TriageService triage;

    record Review(String review) {
    }

    @POST
    public TriagedReview triage(Review review) {
        return triage.triage(review.review());
    }
}
