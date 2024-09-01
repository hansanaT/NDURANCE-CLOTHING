package com.ndurance.wish_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class WishListEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String wishId;
    private String productId;
    private int price;
    private String user;
}
