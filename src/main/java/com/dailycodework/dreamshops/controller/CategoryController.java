package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.category.CategoryService;
import com.dailycodework.dreamshops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categoryList = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Categories fetched successfully", categoryList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch categories", e.getMessage()));
        }
    }

    // Add category
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        try {
            Category theCategory = categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Category added successfully", theCategory));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Failed/Conflict to add category", e.getMessage()));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        // can be performed using Optional and without try catch block
        return Optional.ofNullable(categoryService.getCategoryById(id))
        .map(category -> ResponseEntity.ok(new ApiResponse("Category fetched successfully", category)))
        .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category not found", NOT_FOUND)));
    }

    // Category by name
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        return Optional.ofNullable(categoryService.getCategoryByName(name))
        .map(category -> ResponseEntity.ok(new ApiResponse("Category fetched successfully", category)))
        .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category not found", NOT_FOUND)));
    }

    // Delete category
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        // can be done use optional but let us do it with try catch block
        try {
//            Category categoryToBeDeleted = categoryService.getCategoryById(id); // no need for categorynot found as
//            already present in getcategorybyid
//            if(categoryToBeDeleted!=null){
                categoryService.deleteCategoryById(id);
                return ResponseEntity.ok(new ApiResponse(("Deleted category successfully"), null));
//            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Catefory not found", e.getMessage()));
        }
//        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Category not found", INTERNAL_SERVER_ERROR));
    }

    // Update category
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Category updated successfully", updatedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category not found", e.getMessage()));
        }
    }

}
