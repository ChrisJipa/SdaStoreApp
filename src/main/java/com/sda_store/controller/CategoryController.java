package com.sda_store.controller;

import com.sda_store.controller.dto.category.CategoryRequestDto;
import com.sda_store.controller.dto.category.CategoryResponseDto;
import com.sda_store.model.Category;
import com.sda_store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponseDto create(@RequestBody CategoryRequestDto dto) {
        Category category = categoryService.create(mapCategoryRequestDtoToCategory(dto));
        return mapCategoryToCategoryResponseDto(category);
    }

    @GetMapping
    public List<CategoryResponseDto> findAllRootCategories(){
        List<Category> categoryList = categoryService.findAllRootCategories();
        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();
        for (Category c : categoryList) {
            categoryResponseDtoList.add(mapCategoryToCategoryResponseDto(c));
        }
        return categoryResponseDtoList;
    }

    @GetMapping(path = "/all")
    public List<CategoryResponseDto> findAll() {
        return categoryService
                .findAll()
                .stream()
                .map(this::mapCategoryToCategoryResponseDtoWithIdAndName)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/{id}")
    public CategoryResponseDto update(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        Category category = categoryService.findById(id); // obiect initial
        Category updatedCategory = updateCategoryFromCategoryRequestDto(category, categoryRequestDto); // obiect updatat dar nu salvat in baza de date
        return mapCategoryToCategoryResponseDto(categoryService.update(updatedCategory)); // salvam obiectul updatat si il returnam
    }


    public Category updateCategoryFromCategoryRequestDto(Category existentCategory, CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto.getName() != null) {
            existentCategory.setName(categoryRequestDto.getName());
        }
        if (categoryRequestDto.getParentId() != null) {
            Category newParentCategory = categoryService.findById(categoryRequestDto.getParentId());
            if (existentCategory.getId().equals(newParentCategory.getId())) {
                throw new RuntimeException(String.format("Id of the parent can't be the same as the id of the resource"));
            }
            existentCategory.setParent(newParentCategory);
        }
        return existentCategory;
    }

    public Category mapCategoryRequestDtoToCategory(CategoryRequestDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        if (dto.getParentId() != null) {
            Category parent = categoryService.findById(dto.getParentId());
            category.setParent(parent);
        }
        return category;
    }

    /**
     * In DB :
     * 3 categorry
     * {
     *     id: 1
     *     name: parent
     *     subcategories : [{
     *         id: 2,
     *         name: subCategory1,
     *         subcategories: [{
     *             id:3,
     *             name: subCategory2,
     *             subcatgories: [{}]
     *         }
     *     }]
     * }
     *
     *
     *
     */
    public CategoryResponseDto mapCategoryToCategoryResponseDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getParent() != null) {
            dto.setParentName(category.getParent().getName());
        }
        List<Category> subCategories = category.getSubCategories();
        if (subCategories != null) {
            List<CategoryResponseDto> subCategoriesDto = new ArrayList<>();
            for (Category cat : subCategories) {
                subCategoriesDto.add(mapCategoryToCategoryResponseDto(cat));
            }
            dto.setSubCategories(subCategoriesDto);
        }
        return dto;
    }

    public CategoryResponseDto mapCategoryToCategoryResponseDtoWithIdAndName(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setId(category.getId());
        return categoryResponseDto;
    }
}
