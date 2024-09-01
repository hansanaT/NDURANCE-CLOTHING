package com.ndurance.car_service.shared.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRequestModel {
    private String products;;
    private int price;
}
