package com.skndan.model.record;
import com.fasterxml.jackson.annotation.JsonCreator;

public record TestReview(String evaluation, String message) {
    @JsonCreator
    public TestReview {
    }
}