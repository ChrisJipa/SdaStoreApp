package com.sda_store.service.implementation;

import com.sda_store.exception.ResourceNotFoundInDatabase;
import com.sda_store.model.Category;
import com.sda_store.model.Product;
import com.sda_store.repository.CategoryRepository;
import com.sda_store.repository.ProductRepository;
import com.sda_store.repository.ProductSpecification;
import com.sda_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImplementation(ProductRepository repository, CategoryRepository categoryRepository){
        this.productRepository = repository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Product create(Product product) {
      return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new ResourceNotFoundInDatabase(String.format("Product with id %d does not exist", id));
        }
    }


    @Override
    public Product update(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setThumbnail(product.getThumbnail());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setProductType(product.getProductType());
        existingProduct.setUser(product.getUser());
        return productRepository.save(existingProduct);
    }

    // {"name": "some-name"},
    //  {"productType": "some-product-type"}
    @Override
    public Page<Product> searchProducts(Map<String, String> params) {
        Specification<Product> specification = new ProductSpecification();

        if (params.get("page") == null || params.get("pageSize") == null) {
            throw new RuntimeException("page and pageSize must have valued!");
        }

        Integer page = Integer.valueOf(params.get("page"));
        Integer pageSize = Integer.valueOf(params.get("pageSize"));

        // iteram prin key-urile map-ului
        for (String parameterName : params.keySet()) {
            if(parameterName.equals("categoryId")) {

                List<Long> categoryIds = getCategoryIds(Long.valueOf(params.get(parameterName)));

                specification =
                        specification
                                .and(ProductSpecification.getSpecificationByParameterWithMultipleValues(parameterName, categoryIds));
            } else {
                specification =
                        specification
                                .and(ProductSpecification.getSpecificationByParameter(parameterName, params.get(parameterName)));
            }
        }

        return productRepository.findAll(specification, PageRequest.of(page, pageSize));
    }

    private List<Long> getCategoryIds(Long parentCategoryId) {
        //gasim categoria in DB
        Category category = categoryRepository.findById(parentCategoryId).get(); //Metoda intoarce un optional -> get() este on fortare, pentru ca stim ca intoarce cu siguranta ceva
        List<Long> categoryIds = new ArrayList<>();

        //retinem ID-ul ei
        categoryIds.add(category.getId());

        //verificam daca are subcategorii
        if (category.getSubCategories() != null) {
            List<Category> subCategories = category.getSubCategories();
            //iteram prin subcategorii si adaugam id-ul lor in lista
            subCategories.forEach(subCategory -> categoryIds.addAll(getCategoryIds(subCategory.getId())));
        }
        return categoryIds;
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
