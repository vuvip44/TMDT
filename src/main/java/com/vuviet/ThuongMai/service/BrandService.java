package com.vuviet.ThuongMai.service;

import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Brand;
import com.vuviet.ThuongMai.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public ResultPageDTO getAllBrands(Specification<Brand> spec, Pageable pageable) {
        Page<Brand> pageBrands = brandRepository.findAll(spec, pageable);
        ResultPageDTO res = new ResultPageDTO();
        ResultPageDTO.Meta mt = new ResultPageDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(pageBrands.getTotalPages());
        mt.setTotalElement(pageBrands.getTotalElements());

        res.setMeta(mt);
        res.setResult(pageBrands.getContent());
        return res;
    }

    public Brand getBrand(Long id) {
        return this.brandRepository.findById(id).orElse(null);
    }

    public void deleteBrand(Long id) {
        this.brandRepository.deleteById(id);
    }

    public Brand updateBrand(Brand brandDTO) {
        Brand brand = this.getBrand(brandDTO.getId());
        brand.setName(brandDTO.getName());
        brand.setActive(brandDTO.isActive());
        return this.brandRepository.save(brand);
    }

    public boolean existBrand(Brand brand) {
        return this.brandRepository.existsByName(brand.getName());
    }
}
