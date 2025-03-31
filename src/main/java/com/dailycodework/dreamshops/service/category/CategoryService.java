package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // used for constructor injection but make it final because it is immutable
// autowired is not used here because we are using constructor injection
public class CategoryService implements ICategoryService{

    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(
                        ()-> new AlreadyExistsException("Category with name: " + category.getName() + " already exists")
                );
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Category with id: " + id + " not found")
        );
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return categoryRepository.findById(id).map(
                oldCatergory -> {
                    oldCatergory.setName(category.getName());
                    return categoryRepository.save(oldCatergory);
                }
        ).orElseThrow(
                ()-> new ResourceNotFoundException("Category with id: " + id + " not found")
        );
    }

    @Override
    public void deleteCategoryById(long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository :: delete,
                ()-> {
                    throw new ResourceNotFoundException("Category with id: " + id + " not found");
                }
        );
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
}
