package com.example.ThuongMai.service;

import com.example.ThuongMai.dto.ResultPageDTO;
import com.example.ThuongMai.entity.Category;
import com.example.ThuongMai.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category categoryDTO) {
        return categoryRepository.save(categoryDTO);
    }

    public ResultPageDTO getAllCategories(Specification<Category> spec, Pageable pageable) {
        Page<Category> pageCategory = categoryRepository.findAll(spec, pageable);

        ResultPageDTO res=new ResultPageDTO();
        ResultPageDTO.Meta mt=new ResultPageDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(pageCategory.getTotalPages());
        mt.setTotalElement(pageCategory.getTotalElements());

        res.setMeta(mt);
        res.setResult(pageCategory.getContent());
        return res;
    }

    public Category getById(long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Category categoryDTO) {
        Category category = this.getById(categoryDTO.getId());

        category.setName(categoryDTO.getName());

        category.setActive(categoryDTO.isActive());
        return categoryRepository.save(category);
    }

    public boolean isExistCategory(Category category) {
        return categoryRepository.existsByName(category.getName());
    }
}
