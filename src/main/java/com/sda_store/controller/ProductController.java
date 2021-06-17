package com.sda_store.controller;

import com.sda_store.controller.dto.product.ProductRequestDto;
import com.sda_store.controller.dto.product.ProductResponseDto;
import com.sda_store.model.Category;
import com.sda_store.model.Product;
import com.sda_store.model.ProductType;
import com.sda_store.service.CategoryService;
import com.sda_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/products")
    public ProductRequestDto create(@RequestBody ProductRequestDto ProductRequestDto) {
        Product product = productService.create(mapProductRequestDtoToProduct(ProductRequestDto));
        return mapProductToProductResponseDto(product);
    }

    @PutMapping(value = "/products/{id}")
    public ProductRequestDto update(@PathVariable("id") Long id, @RequestBody ProductRequestDto ProductRequestDto) {
        Product updatedProduct = updateProductRequestDtoToProduct(productService.findById(id), ProductRequestDto);
        return mapProductToProductResponseDto(productService.create(updatedProduct));
    }

    @GetMapping(value = "/products")
    public Page<ProductResponseDto> searchProducts(@RequestParam Map<String, String> params) {
        Page<Product> productList = productService.searchProducts(params);

        return new PageImpl<ProductResponseDto>(
                productList
                        .getContent()
                        .stream()
                        .map(this::mapProductToProductResponseDto)
                        .collect(Collectors.toList()),
                productList.getPageable(),
                productList.getTotalElements()
        );
    }

    @GetMapping(value = "/products/{id}")
    public ProductRequestDto findById(@PathVariable("id") Long id) {
        return mapProductToProductResponseDto(productService.findById(id));
    }

    @GetMapping(value = "/product-types")
    public List<ProductType> getProductTypes() {
        return Arrays.asList(ProductType.values());
    }

    private ProductResponseDto mapProductToProductResponseDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setThumbnail(product.getThumbnail());
        productResponseDto.setCategoryId(product.getCategory().getId());
        productResponseDto.setProductType(product.getProductType());
        productResponseDto.setUser(product.getUser());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setId(product.getId());
        productResponseDto.setCategoryName(product.getCategory().getName());
        productResponseDto.setStock(product.getStock());
        return productResponseDto;
    }

    private Product mapProductRequestDtoToProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setThumbnail(productRequestDto.getThumbnail());

        Category category = categoryService.findById(productRequestDto.getCategoryId());
        product.setCategory(category);

        product.setProductType(productRequestDto.getProductType());
        product.setUser(productRequestDto.getUser());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());

        return product;
    }

    private Product updateProductRequestDtoToProduct(Product product, ProductRequestDto productRequestDto) {
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setThumbnail(productRequestDto.getThumbnail());

        Category category = categoryService.findById(productRequestDto.getCategoryId());
        product.setCategory(category);

        product.setProductType(productRequestDto.getProductType());
        product.setUser(productRequestDto.getUser());
        product.setStock(productRequestDto.getStock());

        return product;
    }
}
