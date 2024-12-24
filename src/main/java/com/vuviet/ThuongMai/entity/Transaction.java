package com.vuviet.ThuongMai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionno;
    private String transactionstatus;
    private String banktranno;
    private String txnref;
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;
}
