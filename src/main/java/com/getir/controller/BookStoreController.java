package com.getir.controller;

import com.getir.exception.ForbiddenException;
import com.getir.exception.IllegalArgumentApplicationException;
import com.getir.exception.NotFoundException;
import com.getir.model.*;
import com.getir.security.JwtUtils;
import com.getir.service.CustomerService;
import com.getir.service.OrderService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookStoreController {

    final
    CustomerService customerService;
    final
    OrderService orderService;

    public BookStoreController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @Timed
    @Counted
    @Operation(summary = "Create a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created the customer"),
            @ApiResponse(responseCode = "400", description = "Invalid field supplied",
                    content = @Content)})
    @RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomer(@Valid @RequestBody final PostCustomerRequest request, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new IllegalArgumentApplicationException(StringUtils.join(result.getAllErrors(), ","));
        }
        String email = customerService.createCustomer(request);
         return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, email).build();
    }

    @Timed
    @Counted
    @Operation(summary = "Create a order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created the order"),
            @ApiResponse(responseCode = "400", description = "Invalid field supplied",
                    content = @Content)})
    @RequestMapping(value = "/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOrder(@Valid @RequestBody final PostOrderRequest request, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new IllegalArgumentApplicationException(StringUtils.join(result.getAllErrors(), ","));
        }
        long orderId = orderService.createNewOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(orderId)).build();
    }

    @Timed
    @Counted
    @Operation(summary = "List orders of a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "orders of a specific customer returned successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCustomerOrdersResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)})
    @RequestMapping(value = "/orders/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCustomerOrders(@RequestParam("customer_email") String customerEmail) {
        GetCustomerOrdersResponse response = orderService.getCustomerOrders(customerEmail);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Timed
    @Counted
    @Operation(summary = "List detail of an order by a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details returned successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetOrderDetailsResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found with the given id",
                    content = @Content)})
    @RequestMapping(value = "/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOrderDetails(@RequestParam("id") Long orderId) {
        GetOrderDetailsResponse response = orderService.getOrderDetails(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Timed
    @Counted
    @Operation(summary = "Login as a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token has been issued for the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetOrderDetailsResponse.class))})})
    @Valid
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestParam("username") @Size(min = 2) String username) {
        String token = JwtUtils.getJWTToken(username);
        LoginResponse response = new LoginResponse();
        response.setUser(username);
        response.setToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setField(ex.getParameterName());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException notFoundException) {
        ErrorResponse error = new ErrorResponse();
        error.setGot(notFoundException.getGot());
        error.setMessage(notFoundException.getMessage());
        error.setField(notFoundException.getField());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentApplicationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentApplicationException(IllegalArgumentApplicationException illegalArgumentApplicationException) {
        ErrorResponse error = new ErrorResponse();
        error.setGot(illegalArgumentApplicationException.getGot());
        error.setMessage(illegalArgumentApplicationException.getMessage());
        error.setField(illegalArgumentApplicationException.getField());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentApplicationException(ForbiddenException forbiddenException) {
        ErrorResponse error = new ErrorResponse();
        error.setGot(forbiddenException.getGot());
        error.setMessage(forbiddenException.getMessage());
        error.setField(forbiddenException.getField());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

}
