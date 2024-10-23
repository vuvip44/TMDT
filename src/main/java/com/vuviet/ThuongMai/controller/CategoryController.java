package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Category;
import com.vuviet.ThuongMai.service.CategoryService;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import com.vuviet.ThuongMai.util.error.IdInValidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage("Create a category")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid Category category) throws IdInValidException {
        if(this.categoryService.isExistCategory(category)){
            throw new IdInValidException("Category "+category.getName()+" đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Get category by id")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) throws IdInValidException {
        Category category=this.categoryService.getById(id);
        if(category==null){
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/categories")
    @ApiMessage("Get all categories")
    public ResponseEntity<ResultPageDTO> getAllCategories(
            @Filter Specification<Category> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(categoryService.getAllCategories(spec, pageable));
    }

    @PutMapping("/categories")
    @ApiMessage("Update a category")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws IdInValidException {
        if(this.categoryService.getById(category.getId())==null){
            throw new IdInValidException("Id "+category.getId()+" không tồn tại");
        }
        if(this.categoryService.isExistCategory(category)){
            throw new IdInValidException("Category "+category.getName()+" đã tồn tại");
        }
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }
}
