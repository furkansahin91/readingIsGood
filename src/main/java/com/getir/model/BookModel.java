package com.getir.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "isbn",
        "price"
})
@Getter
@Setter
public class BookModel implements Serializable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("price")
    private double price;
}
