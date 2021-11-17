package com.getir.service;

import com.getir.entity.Customer;
import com.getir.exception.IllegalArgumentApplicationException;
import com.getir.model.PostCustomerRequest;
import com.getir.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerService {

    final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String createCustomer(PostCustomerRequest request) {
        if (findCustomerByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentApplicationException("A customer with the given email is already registered", request.getEmail(), "email");
        }
        Customer customer = new Customer();
        customer.setAddress(request.getAddress());
        customer.setChangeTimestamp(new Date());
        customer.setEmail(request.getEmail());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setUsername(request.getUsername());
        final Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getEmail();
    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

}
