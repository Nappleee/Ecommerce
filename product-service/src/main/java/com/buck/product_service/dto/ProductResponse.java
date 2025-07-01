package com.buck.product_service.dto;

import com.buck.product_service.model.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private int stock;
    private Category category;
    private String imageUrl;
    private String sku;
    private Double price;
}
