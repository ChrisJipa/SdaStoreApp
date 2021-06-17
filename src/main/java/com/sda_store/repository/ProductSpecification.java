package com.sda_store.repository;

import com.sda_store.model.Category;
import com.sda_store.model.Product;
import com.sda_store.model.ProductType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

public class ProductSpecification implements Specification<Product> {

    public static Specification<Product> withNameLike(String productName) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + productName + "%");
    }

    public static Specification<Product> ofType(ProductType productType) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("productType"), productType);
    }

    public static Specification<Product> withPriceInRange(Double lowerInterval, Double higherInterval) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), lowerInterval, higherInterval);
    }

    public static Specification<Product> withCategoryId(Long categoryId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("id"), categoryId);
        };
    }

    public static Specification<Product> withCategoryIdsIn(List<Long> categoryIds) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            //root este obiectul Product
            Join<Product, Category> categoryJoin = root.join("category");
            return categoryJoin.get("id").in(categoryIds);
        };
    }

    // http://localhost:8080/products?page=0&pageSize=10&price=lowPrice:50|highPrice:100
    public static Specification<Product> getSpecificationByParameter(String parameterName, String parameterValue) {
        switch (parameterName) {
            case "name":
                return withNameLike(parameterValue);
            case "productType":
                return ofType(ProductType.valueOf(parameterValue));
            case "categoryId":
                return withCategoryId(Long.valueOf(parameterValue));
            // din frontend price=100:200
            // => parameterName = price
            // => parameterValue = 100:200
            // "100:200".split(":") rezulta ["100", "200"]
            // "100:"
            case "price":
                String price = parameterValue; // lowPrice:50,highPrice:100; // price=highPrice:100,lowPrice:50
                Double lowPrice = 0.0;
                Double highPrice = Double.MAX_VALUE;
                if (price.contains(",")) {
                    List<String> prices = Arrays.asList(price.split(",")); // lowPrice:50 | highPrice:100
                    for (String iteration : prices) {
                        if (iteration.contains("lowPrice")) {
                            String lowPriceInStringFormat = iteration.replaceAll("lowPrice:", ""); // lowPrice:50  ===> 50;
                            lowPrice = Double.valueOf(lowPriceInStringFormat);
                        } else if (iteration.contains("highPrice")) {
                            {
                                String highPriceInStringFormat = iteration.replaceAll("highPrice:", ""); // highPrice:100 ==> 100;
                                highPrice = Double.valueOf(highPriceInStringFormat);
                            }
                        }
                    }
                }
                return withPriceInRange(lowPrice, highPrice);
            default:
                return new ProductSpecification();
        }
    }

    public static Specification<Product> getSpecificationByParameterWithMultipleValues(String parameterName, List<Long> ids) {
        switch (parameterName) {
            case "categoryId": return withCategoryIdsIn(ids);
            default: return new ProductSpecification();
        }
    }

    @Override
    public Specification<Product> and(Specification<Product> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Product> or(Specification<Product> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
