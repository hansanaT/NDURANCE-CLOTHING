package com.ndurance.wish_list.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishListDTO {
    private Long id;
    private String wishId;
    private String productId;
    private int price;
    private String user;
}
