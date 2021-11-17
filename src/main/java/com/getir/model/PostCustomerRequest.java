package com.getir.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "first_name",
        "last_name",
        "username",
        "email",
        "phone_number",
        "address"
})
@Getter
@Setter
public class PostCustomerRequest implements Serializable {
    @JsonProperty("first_name")
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;
    @JsonProperty("last_name")
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;
    @JsonProperty("username")
    @NotNull
    @Size(min = 2, max = 50)
    private String username;
    @JsonProperty("email")
    @NotNull
    @Size(max = 50, min = 7)
    @Pattern(regexp = "^$|^.*@.*\\..*$")
    private String email;
    @JsonProperty("phone_number")
    @NotNull
    private long phoneNumber;
    @JsonProperty("address")
    @NotNull
    @Size(max = 255)
    private String address;

}
