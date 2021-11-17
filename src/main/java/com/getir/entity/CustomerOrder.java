package com.getir.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Data
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Audited
    private long id;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
    private List<BookOrder> bookOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @Audited
    private Customer customer;

    @Column(name = "total_amount")
    @Audited
    private Double totalAmount;

    @Column(name = "change_timestamp")
    private LocalDateTime changeTimestamp;

    @Column(name = "changed_by")
    private String changedBy;

}
