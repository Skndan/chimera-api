package com.skndan.model.record;
import com.fasterxml.jackson.annotation.JsonCreator;

public record TriagedReview(String evaluation, String message) {
    @JsonCreator
    public TriagedReview {
    }
}