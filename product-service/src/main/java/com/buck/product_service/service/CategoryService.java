package com.buck.product_service.service;

import com.buck.product_service.dto.CategoryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();

    Page<CategoryDTO> findAllCategory(int page, int size);

    List<CategoryDTO> getAllCategories(Integer pageNo, Integer pageSize, String sortBy);

    CategoryDTO findById(Integer categoryId);

    CategoryDTO save(CategoryDTO categoryDto);

    CategoryDTO update(CategoryDTO categoryDto);

    CategoryDTO update(Integer categoryId, CategoryDTO categoryDto);

    void deleteById(Integer categoryId);

}
