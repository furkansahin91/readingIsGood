package com.getir.repository;

import com.getir.entity.CustomerOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<CustomerOrder, Long> {
    List<CustomerOrder> findOrdersByCustomer_Id(long customerId);
}
