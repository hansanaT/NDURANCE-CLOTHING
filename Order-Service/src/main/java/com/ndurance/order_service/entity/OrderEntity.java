package com.ndurance.order_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String orderId;
    @OneToMany
    private List<ProductEntity> products;
    private String user;
    private Long totalPrice;
    private Date orderDate;
    private boolean isRefund;
    private boolean isDelivered;
    @OneToOne
    private AddressEntity shippingAddress;
    @OneToOne
    private AddressEntity billingAddress;
}
