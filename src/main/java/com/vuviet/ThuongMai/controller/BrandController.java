package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Brand;
import com.vuviet.ThuongMai.service.BrandService;
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
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/brands")
    @ApiMessage("Create a brand")
    public ResponseEntity<Brand> createBrand(@RequestBody @Valid Brand brand) throws IdInValidException {
        if(this.brandService.existBrand(brand)){
            throw new IdInValidException("Brand "+brand.getName()+" đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.brandService.createBrand(brand));
    }

    @GetMapping("/brands/{id}")
    @ApiMessage("Get brand by id")
    public ResponseEntity<Brand> getBrandById(@PathVariable long id) throws IdInValidException {
        Brand brand=this.brandService.getBrand(id);
        if(brand==null){
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        return ResponseEntity.ok().body(brand);
    }

    @PutMapping("/brands")
    @ApiMessage("Update a brand")
    public ResponseEntity<Brand> updateBrand(@RequestBody Brand brand) throws IdInValidException {
        if(this.brandService.getBrand(brand.getId())==null){
            throw new IdInValidException("Brand "+brand.getId()+" không tồn tại");
        }
        if(this.brandService.existBrand(brand)){
            throw new IdInValidException("Brand "+brand.getName()+" đã tồn tại");
        }
        return ResponseEntity.ok().body(this.brandService.updateBrand(brand));
    }

    @GetMapping("/brands")
    @ApiMessage("Get all brands")
    public ResponseEntity<ResultPageDTO> getAllBrands(
            @Filter Specification<Brand> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(this.brandService.getAllBrands(spec, pageable));
    }
}
