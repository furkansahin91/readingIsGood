package com.getir.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "got",
        "message",
        "field"
})
@Getter
@Setter
public class ErrorResponse {
    private String got;
    private String message;
    private String field;
}
