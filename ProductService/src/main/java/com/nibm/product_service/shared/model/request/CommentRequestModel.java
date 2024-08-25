package com.nibm.product_service.shared.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestModel {
    private String comment;
    private String email;
    private String clothId;
}
