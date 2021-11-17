package com.getir.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "isbn", length = 13, nullable = false)
    private String isbn;

    @Column(name = "in_stock")
    private Integer inStock;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "book")
    private List<BookOrder> bookOrders;

}
