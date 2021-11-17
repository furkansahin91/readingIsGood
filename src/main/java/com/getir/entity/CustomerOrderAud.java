package com.getir.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "customer_order_aud")
@Data
public class CustomerOrderAud {
    @EmbeddedId
    private CustomerOrderAudPK id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rev", insertable = false, updatable = false)
    private RevisionInfo revisionInfo;
    @Column(name = "book_id")
    private Integer bookId;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "total_amount")
    private Double totalAmount;
}
