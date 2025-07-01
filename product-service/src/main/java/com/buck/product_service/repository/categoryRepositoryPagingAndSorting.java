package com.buck.product_service.repository;

import com.buck.product_service.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface categoryRepositoryPagingAndSorting extends PagingAndSortingRepository<Category, Integer> {
    @Query("SELECT c FROM Category c")
    Page<Category> findAllPagedAndSortedCategories(Pageable pageable);
}
