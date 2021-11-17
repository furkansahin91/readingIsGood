package com.getir.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "customer_aud")
@Data
public class CustomerAud {

    @EmbeddedId
    private CustomerAudPK id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rev", insertable = false, updatable = false)
    private RevisionInfo revisionInfo;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    private String email;
    @Column(name = "phone_number")
    private long phoneNumber;
    private String address;
    @Column(name = "order_id")
    private Integer orderId;
}
