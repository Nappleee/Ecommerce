package com.buck.product_service.service.impl;

import com.buck.product_service.dto.ProductRequest;
import com.buck.product_service.dto.ProductResponse;
import com.buck.product_service.model.Category;
import com.buck.product_service.model.Product;
import com.buck.product_service.repository.CategoryRepository;
import com.buck.product_service.repository.ProductRepository;
import com.buck.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    // mapping method
    private ProductResponse mapToDTO(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .sku(product.getSku())
                .category(product.getCategory())
                .build();
    }

    private Product mapToEntity(ProductRequest dto) {
        Category category = categoryRepository.findById(dto.getCategory().longValue())
                .orElseThrow(() -> new RuntimeException("Category not found with id " + dto.getCategory().longValue()));

        return Product.builder().name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .imageUrl(dto.getImageUrl())
                .sku(dto.getSku())
                .category(category)
                .build();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id:" + id));
        return mapToDTO(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ProductResponse save (ProductRequest dto) {
        Product product = mapToEntity(dto);
        Product saved = productRepository.save(product);
        return mapToDTO(saved);
    }


    @Override
    public ProductResponse update(Long id, ProductRequest dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
        Category category = categoryRepository.findById(dto.getCategory().longValue())
                .orElseThrow(() -> new RuntimeException("Category not found with id " + dto.getCategory().longValue()));
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setSku(dto.getSku());
        product.setCategory(category);

        Product updated = productRepository.save(product);
        return mapToDTO(updated);
    }
    @Override
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm với id: " + id);
        }
        productRepository.deleteById(id);
    }
}
