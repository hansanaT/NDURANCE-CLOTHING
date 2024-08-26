package com.ndurance.product_service.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClothDTO {
    private Long id;
    private String publicId;
    private String name;
    private String description;
    private List<CommentDTO> comments;
    private List<String> images;

}
