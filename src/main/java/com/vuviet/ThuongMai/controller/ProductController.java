package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.requestdto.ReqCreateProductDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResCreateProductDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResProductDetailDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Product;
import com.vuviet.ThuongMai.service.BrandService;
import com.vuviet.ThuongMai.service.CategoryService;
import com.vuviet.ThuongMai.service.ProductService;
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
public class ProductController {
    private final ProductService productService;

    private final CategoryService categoryService;

    private final BrandService brandService;

    public ProductController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @PostMapping("/admin/products")
    @ApiMessage("Create a product")
    public ResponseEntity<ResCreateProductDTO> createProduct(@RequestBody @Valid ReqCreateProductDTO req) throws IdInValidException {
        if(this.productService.isExistProduct(req)){
            throw new IdInValidException("Sản phẩm "+req.getName()+" đã tồn tại");
        }

        if(this.brandService.getBrand(req.getBrandId()) == null){
            throw new IdInValidException("Sản phẩm thuộc brand: "+req.getBrandId()+" không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.createProduct(req));
    }

    @PutMapping("/admin/products")
    @ApiMessage("Update a product")
    public ResponseEntity<ResCreateProductDTO> updateProduct(@RequestBody ReqCreateProductDTO req) throws IdInValidException {
        if(this.productService.getById(req.getId()) == null){
            throw new IdInValidException("Id "+req.getId()+" không tồn tại");
        }

        if(this.brandService.getBrand(req.getBrandId()) == null){
            throw new IdInValidException("Sản phẩm thuộc brand: "+req.getBrandId()+" không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.updateProduct(req));
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Get product detail by id")
    public ResponseEntity<ResProductDetailDTO> getProductDetail(@PathVariable("id") long id) throws IdInValidException {
        if(this.productService.getById(id) == null){
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.getProductDetail(id));
    }

    @GetMapping("/admin/products")
    @ApiMessage("Get all product by admin")
    public ResponseEntity<ResultPageDTO> getAllProductsByAdmin(
            @Filter Specification<Product> spec,
            Pageable pageable
            ){
        return ResponseEntity.ok(this.productService.getAllProduct(spec, pageable));
    }

    @DeleteMapping("/admin/products/{id}")
    @ApiMessage("Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id) throws IdInValidException {
        if(this.productService.getById(id) == null){
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        this.productService.deleteProduct(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/products")
    @ApiMessage("Get all product by user")
    public ResponseEntity<ResultPageDTO> getAllProductsByUser(
            @Filter Specification<Product> spec,
            Pageable pageable
    ){
        return ResponseEntity.ok(this.productService.getAllShortProduct(spec, pageable));
    }
}
