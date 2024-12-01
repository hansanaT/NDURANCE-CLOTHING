package com.ndurance.mobileapp.model.response;

import com.ndurance.mobileapp.model.dto.Product;

import java.util.List;

public class ProductResponse {
    private List<Product> content;
    private int totalPages;

    public List<Product> getContent() {
        return content;
    }

    public void setContent(List<Product> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
