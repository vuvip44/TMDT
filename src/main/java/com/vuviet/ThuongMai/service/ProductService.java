package com.vuviet.ThuongMai.service;

import com.vuviet.ThuongMai.dto.requestdto.ReqCreateProductDTO;
import com.vuviet.ThuongMai.dto.requestdto.ResShortProductDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResCreateProductDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResProductDetailDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Brand;
import com.vuviet.ThuongMai.entity.Category;
import com.vuviet.ThuongMai.entity.Product;
import com.vuviet.ThuongMai.repository.BrandRepository;
import com.vuviet.ThuongMai.repository.CategoryRepository;
import com.vuviet.ThuongMai.repository.ProductRepository;
import com.vuviet.ThuongMai.util.constant.TypeDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResCreateProductDTO createProduct(ReqCreateProductDTO productDTO) {
        Product product = new Product();
        ResCreateProductDTO res = new ResCreateProductDTO();

        ResCreateProductDTO.BrandProduct resBrand = new ResCreateProductDTO.BrandProduct();

        if(productDTO.getCategoryIds()!=null){
            List<Category> categories = this.categoryRepository.findByIdIn(productDTO.getCategoryIds());
            product.setCategories(categories);
            List<ResCreateProductDTO.CategoryProduct> categoryProducts = new ArrayList<>();
            for(Category category : categories){
                ResCreateProductDTO.CategoryProduct categoryProductDTO = new ResCreateProductDTO.CategoryProduct();
                categoryProductDTO.setId(category.getId());
                categoryProductDTO.setName(category.getName());
                categoryProducts.add(categoryProductDTO);
            }
            res.setCategoryProducts(categoryProducts);
        }

        Brand brand = this.brandRepository.findById(productDTO.getBrandId()).get();
        product.setBrand(brand);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPriceUnit(productDTO.getPriceUnit());
        product.setUnitInStock(productDTO.getQuantity());
        product.setTypeDiscount(productDTO.getTypeDiscount());
        product.setDiscount(productDTO.getDiscount());
        product.setImageUrl(productDTO.getImageUrl());
        if(productDTO.getTypeDiscount()== TypeDiscount.PERCENT){
            double result=productDTO.getPriceUnit() * (1 - ((double) productDTO.getDiscount() / 100));
            long kq=(long) Math.round(result);
            product.setSales(kq);
        } else if(productDTO.getTypeDiscount()==TypeDiscount.NUMBER){
            product.setSales(productDTO.getPriceUnit() - productDTO.getDiscount());
        }
        System.out.println(product.getSales());

        this.productRepository.save(product);

        resBrand.setId(product.getBrand().getId());
        resBrand.setName(product.getBrand().getName());
        res.setBrandProduct(resBrand);

        res.setId(product.getId());
        res.setName(product.getName());
        res.setDescription(product.getDescription());
        res.setPriceUnit(product.getPriceUnit());
        res.setUnitInStock(product.getUnitInStock());
        res.setDiscount(product.getDiscount());
        res.setSales(product.getSales());
        res.setImageUrl(product.getImageUrl());
        res.setTypeDiscount(productDTO.getTypeDiscount());
        res.setCreatedAt(product.getCreatedAt());
        res.setUpdatedAt(product.getUpdatedAt());
        res.setCreatedBy(product.getCreatedBy());
        res.setUpdatedBy(product.getUpdatedBy());

        return res;
    }

    public boolean isExistProduct(ReqCreateProductDTO productDTO) {
        return this.productRepository.existsByName(productDTO.getName());
    }

    public Product getById(long id) {
        return this.productRepository.findById(id).get();
    }

    public ResCreateProductDTO updateProduct(ReqCreateProductDTO productDTO) {
        Product product = this.getById(productDTO.getId());
        ResCreateProductDTO res = new ResCreateProductDTO();

        ResCreateProductDTO.BrandProduct resBrand = new ResCreateProductDTO.BrandProduct();

        if(productDTO.getCategoryIds()!=null){
            List<Category> categories = this.categoryRepository.findByIdIn(productDTO.getCategoryIds());
            product.setCategories(categories);
            List<ResCreateProductDTO.CategoryProduct> categoryProducts = new ArrayList<>();
            for(Category category : categories){
                ResCreateProductDTO.CategoryProduct categoryProductDTO = new ResCreateProductDTO.CategoryProduct();
                categoryProductDTO.setId(category.getId());
                categoryProductDTO.setName(category.getName());
                categoryProducts.add(categoryProductDTO);
            }
            res.setCategoryProducts(categoryProducts);
        }

        Brand brand = this.brandRepository.findById(productDTO.getBrandId()).get();
        product.setBrand(brand);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPriceUnit(productDTO.getPriceUnit());
        product.setUnitInStock(productDTO.getQuantity());
        product.setTypeDiscount(productDTO.getTypeDiscount());
        product.setDiscount(productDTO.getDiscount());
        product.setImageUrl(productDTO.getImageUrl());
        if(productDTO.getTypeDiscount()== TypeDiscount.PERCENT){
            double result=productDTO.getPriceUnit() * (1 - ((double) productDTO.getDiscount() / 100));
            long kq=(long) Math.round(result);
            product.setSales(kq);
        } else if(productDTO.getTypeDiscount()==TypeDiscount.NUMBER){
            product.setSales(productDTO.getPriceUnit() - productDTO.getDiscount());
        }
        System.out.println(product.getSales());

        this.productRepository.save(product);

        resBrand.setId(product.getBrand().getId());
        resBrand.setName(product.getBrand().getName());
        res.setBrandProduct(resBrand);

        res.setId(product.getId());
        res.setName(product.getName());
        res.setDescription(product.getDescription());
        res.setPriceUnit(product.getPriceUnit());
        res.setUnitInStock(product.getUnitInStock());
        res.setDiscount(product.getDiscount());
        res.setSales(product.getSales());
        res.setImageUrl(product.getImageUrl());
        res.setTotalSold(product.getTotalSold());
        res.setTypeDiscount(productDTO.getTypeDiscount());
        res.setCreatedAt(product.getCreatedAt());
        res.setUpdatedAt(product.getUpdatedAt());
        res.setCreatedBy(product.getCreatedBy());
        res.setUpdatedBy(product.getUpdatedBy());

        return res;

    }

    public ResProductDetailDTO getProductDetail(long id) {
        ResProductDetailDTO res = new ResProductDetailDTO();
        Product product = this.getById(id);

        res.setName(product.getName());
        res.setDescription(product.getDescription());
        res.setImageUrl(product.getImageUrl());
        res.setPriceUnit(product.getPriceUnit());
        res.setTotalSold(product.getTotalSold());
        res.setUnitInStock(product.getUnitInStock());
        res.setSales(product.getSales());
        res.setImageUrl(product.getImageUrl());
        List<String> nameCategories = product.getCategories()
                .stream().map(x->x.getName()).toList();
        res.setCategories(nameCategories);
        res.setBrand(product.getBrand().getName());
        return res;

    }

    public ResultPageDTO getAllProduct(Specification<Product> spec, Pageable pageable){
        Page<Product> pageProduct=this.productRepository.findAll(spec,pageable);
        ResultPageDTO rs=new ResultPageDTO();
        ResultPageDTO.Meta mt=new ResultPageDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(pageProduct.getTotalPages());
        mt.setTotalElement(pageProduct.getTotalElements());

        rs.setMeta(mt);
        List<Product> products = pageProduct.getContent();
        List<ResCreateProductDTO> resCreateProductDTOs = new ArrayList<>();
        for(Product product : products){
            ResCreateProductDTO resCreateProductDTO = new ResCreateProductDTO();
            resCreateProductDTO.setId(product.getId());
            resCreateProductDTO.setName(product.getName());
            resCreateProductDTO.setDescription(product.getDescription());
            resCreateProductDTO.setImageUrl(product.getImageUrl());
            resCreateProductDTO.setPriceUnit(product.getPriceUnit());
            resCreateProductDTO.setUnitInStock(product.getUnitInStock());
            resCreateProductDTO.setSales(product.getSales());
            resCreateProductDTO.setTypeDiscount(product.getTypeDiscount());
            resCreateProductDTO.setTotalSold(product.getTotalSold());
            resCreateProductDTO.setDiscount(product.getDiscount());

            resCreateProductDTO.setCreatedAt(product.getCreatedAt());
            resCreateProductDTO.setUpdatedAt(product.getUpdatedAt());
            resCreateProductDTO.setCreatedBy(product.getCreatedBy());
            resCreateProductDTO.setUpdatedBy(product.getUpdatedBy());

            List<Category> categories = product.getCategories();
            List<ResCreateProductDTO.CategoryProduct> categoryProducts = new ArrayList<>();
            for(Category category : categories){
                ResCreateProductDTO.CategoryProduct categoryProductDTO = new ResCreateProductDTO.CategoryProduct();
                categoryProductDTO.setId(category.getId());
                categoryProductDTO.setName(category.getName());
                categoryProducts.add(categoryProductDTO);
            }
            resCreateProductDTO.setCategoryProducts(categoryProducts);

            ResCreateProductDTO.BrandProduct brandProduct = new ResCreateProductDTO.BrandProduct();
            brandProduct.setId(product.getBrand().getId());
            brandProduct.setName(product.getBrand().getName());
            resCreateProductDTO.setBrandProduct(brandProduct);

            resCreateProductDTOs.add(resCreateProductDTO);
        }
        rs.setResult(resCreateProductDTOs);
        return rs;
    }

    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public ResultPageDTO getAllShortProduct(Specification<Product> spec, Pageable pageable){
        Page<Product> pageProduct=this.productRepository.findAll(spec,pageable);
        ResultPageDTO rs=new ResultPageDTO();
        ResultPageDTO.Meta mt=new ResultPageDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(pageProduct.getTotalPages());
        mt.setTotalElement(pageProduct.getTotalElements());

        rs.setMeta(mt);
        List<Product> products = pageProduct.getContent();
        List<ResShortProductDTO> resCreateProductDTOs = new ArrayList<>();
        for(Product product : products){
            ResShortProductDTO resCreateProductDTO = new ResShortProductDTO();

            resCreateProductDTO.setName(product.getName());

            resCreateProductDTO.setPrice(product.getPriceUnit());

            resCreateProductDTO.setTotalSold(product.getTotalSold());

            resCreateProductDTOs.add(resCreateProductDTO);
        }
        rs.setResult(resCreateProductDTOs);
        return rs;
    }


}
