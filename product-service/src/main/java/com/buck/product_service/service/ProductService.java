package com.buck.product_service.service;

import com.buck.product_service.dto.ProductRequest;
import com.buck.product_service.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse save(ProductRequest dto);

    ProductResponse update(Long id, ProductRequest dto);

    void deleteById(Long id);
}
