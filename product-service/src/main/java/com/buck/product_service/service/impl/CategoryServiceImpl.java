package com.buck.product_service.service.impl;

import com.buck.product_service.dto.CategoryDTO;
import com.buck.product_service.helper.CategoryMappingHelper;
import com.buck.product_service.model.Category;
import com.buck.product_service.repository.CategoryRepository;
import com.buck.product_service.repository.categoryRepositoryPagingAndSorting;
import com.buck.product_service.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final categoryRepositoryPagingAndSorting categoryRepositoryPagingAndSorting;
    private final ModelMapper modelMapper;
    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category-> CategoryMappingHelper.map(category))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> findAllCategory (int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        // Biến danh sách phần tử category thành categoryDTO để trả cho client và xóa những cái giống nhau
        List<CategoryDTO> dtoList = categoryPage.getContent()
                .stream()
                .map(CategoryMappingHelper::map)
                .distinct()
                .collect(Collectors.toList());
        return  new PageImpl<>(dtoList, pageable, categoryPage.getTotalElements());
    }

    @Override
    public List<CategoryDTO> getAllCategories(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Category> pagedResult = categoryRepositoryPagingAndSorting.findAllPagedAndSortedCategories(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    @Override
    public CategoryDTO findById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId.longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với id: " + categoryId));
        return CategoryMappingHelper.map(category);
    }

    @Override
    public CategoryDTO save(CategoryDTO dto) {
        Category category = CategoryMappingHelper.map(dto);
        Category saved = categoryRepository.save(category);
        return CategoryMappingHelper.map(saved);
    }

    @Override
    public CategoryDTO update(CategoryDTO dto) {
        Category existing = categoryRepository.findById(dto.getCategoryId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với id: " + dto.getCategoryId()));

        BeanUtils.copyProperties(dto, existing, "categoryId", "parentCategoryDto");

        if (dto.getParentCategoryDto() != null) {
            existing.setParentCategory(CategoryMappingHelper.map(dto.getParentCategoryDto()));
        }

        return CategoryMappingHelper.map(categoryRepository.save(existing));
    }

    @Override
    public CategoryDTO update(Integer categoryId, CategoryDTO dto) {
        CategoryDTO existingDto = this.findById(categoryId);
        Category category = CategoryMappingHelper.map(existingDto);
        BeanUtils.copyProperties(dto, category, "categoryId", "parentCategoryDto");

        if (dto.getParentCategoryDto() != null) {
            category.setParentCategory(CategoryMappingHelper.map(dto.getParentCategoryDto()));
        }

        return CategoryMappingHelper.map(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId.longValue())) {
            throw new RuntimeException("Không tìm thấy category với id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId.longValue());
    }
}
