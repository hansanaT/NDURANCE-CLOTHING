package com.ndurance.order_service.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String comment;
    private String userPublicId;
}
