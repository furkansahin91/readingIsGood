package com.getir.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "customer_email",
        "book_isbns"
})
@Getter
@Setter
public class PostOrderRequest implements Serializable {
    @JsonProperty("customer_email")
    @Pattern(regexp = "^$|^.*@.*\\..*$")
    @NotNull
    private String customerEmail;
    @JsonProperty("book_isbns")
    private List<String> bookIsbns;
}
