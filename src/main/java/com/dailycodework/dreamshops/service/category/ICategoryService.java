package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.model.Category;

import java.util.List;

public interface ICategoryService{
    Category addCategory(Category category);
    Category getCategoryById(long id);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(long id);

    List<Category> getAllCategories();
    Category getCategoryByName(String name);

}
