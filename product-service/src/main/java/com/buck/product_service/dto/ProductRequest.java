package com.buck.product_service.dto;

import com.buck.product_service.model.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private int stock;
    private Integer category;      // hiện tại bạn dùng String thay vì Category entity
    private String imageUrl;
    private String sku;
    private Double price;
}
