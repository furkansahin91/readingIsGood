package com.getir.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "total_amount",
        "books"
})
@Getter
@Setter
public class GetOrderDetailsResponse {
    @JsonProperty("id")
    private long id;
    @JsonProperty("books")
    private List<BookModel> books;
    @JsonProperty("total_amount")
    private Double totalAmount;
}
