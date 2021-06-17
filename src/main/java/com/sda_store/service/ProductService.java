package com.sda_store.service;

import com.sda_store.model.Product;
import org.springframework.data.domain.Page;

import java.util.*;

public interface ProductService {
    Product create(Product product);
    Product findById(Long id);
    Product update(Product product);
    Page<Product> searchProducts(Map<String, String> params);
    void delete (Long id);

}
