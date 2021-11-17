package com.getir.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Audited
    private long id;

    @Column(name = "first_name", length = 50, nullable = false)
    @Audited
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    @Audited
    private String lastName;

    @Column(name = "username", length = 50, nullable = false)
    @Audited
    private String username;

    @Column(name = "email", length = 100, nullable = false)
    @Audited
    private String email;

    @Column(name = "phone_number", length = 20, nullable = false)
    @Audited
    private long phoneNumber;

    @Column(name = "address", nullable = false)
    @Audited
    private String address;

    @OneToMany(mappedBy = "customer")
    @Audited
    private List<CustomerOrder> customerOrders;

    @Column(name = "change_timestamp")
    @Temporal(TemporalType.TIME)
    private Date changeTimestamp;

    @Column(name = "changed_by")
    private String changedBy;
}
