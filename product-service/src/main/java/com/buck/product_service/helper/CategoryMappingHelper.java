package com.buck.product_service.helper;

import com.buck.product_service.dto.CategoryDTO;
import com.buck.product_service.model.Category;
import com.buck.product_service.model.Product;

import java.util.Optional;

public class CategoryMappingHelper {

    public static CategoryDTO map(Category category) {
        if (category == null) return null;

        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .imageUrl(category.getImageUrl())
                .parentCategoryDto(
                        Optional.ofNullable(category.getParentCategory())
                                .map(CategoryMappingHelper::map) // đệ quy nếu có parent
                                .orElse(null)
                )
                .build();
    }

    public static Category map(CategoryDTO dto) {
        if (dto == null) return null;

        return Category.builder()
                .categoryId(dto.getCategoryId())
                .categoryTitle(dto.getCategoryTitle())
                .imageUrl(dto.getImageUrl())
                .parentCategory(
                        Optional.ofNullable(dto.getParentCategoryDto())
                                .map(CategoryMappingHelper::map) // đệ quy nếu có parent
                                .orElse(null)
                )
                .build();
    }

}
