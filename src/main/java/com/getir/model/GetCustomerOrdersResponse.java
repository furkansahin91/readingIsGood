package com.getir.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "customer_orders"
})
@Getter
@Setter
public class GetCustomerOrdersResponse implements Serializable {

    @JsonProperty("customer_orders")
    private List<CustomerOrderResponse> customerOrders;

}
