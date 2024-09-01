package com.ndurance.wish_list.shared.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishListRequestModel {
    private String productId;
    private int price;
}
