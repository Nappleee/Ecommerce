package com.buck.product_service.service;

import com.buck.product_service.dto.ProductRequest;
import com.buck.product_service.dto.ProductResponse;
import com.buck.product_service.model.Product;
import com.buck.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory(),
                request.getImageUrl()
        );

        Product saved = productRepository.save(product);

        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getStock(),
                saved.getCategory(),
                saved.getImageUrl()
        );
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> responseList = new ArrayList<>();

        for (Product product : products) {
            responseList.add(new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategory(),
                    product.getImageUrl()
            ));
        }

        return responseList;
    }

    public List<ProductResponse> getByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        List<ProductResponse> responseList = new ArrayList<>();

        for (Product product : products) {
            responseList.add(new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategory(),
                    product.getImageUrl()
            ));
        }

        return responseList;
    }
}
