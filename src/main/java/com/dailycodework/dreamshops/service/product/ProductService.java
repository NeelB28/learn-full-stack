package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // used for constructor injection but make it final because it is immutable
// autowired is not used here because we are using constructor injection
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category exists in DB
        // if yes, set it as new product's category
        // if no, create a new category and set it as new product's category
        // optional is used to handle NullPointerException

        // Extract the category name from the request
        String categoryName = request.getCategory().getName();
        
        // Find or create the category by name
        Category category = Optional.ofNullable(categoryRepository.findByName(categoryName))
                .orElseGet(() -> {
                    Category newCategory = new Category(categoryName);
                    return categoryRepository.save(newCategory);
                });
        
        // Create the product with the managed category entity
        Product addNewProduct = new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
        
        return productRepository.save(addNewProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product with id: " + productId + " not found")
        );
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId).ifPresentOrElse(productRepository::delete,
            ()-> {
                throw new ResourceNotFoundException("Product with id: " + productId + " not found");
            }
        );
    }

    // we are mapping the existing product to the updated product with the help of updateExistingProduct method
    // where ProductUpdateRequest is used to update the existing product
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId).map(
                existingProduct -> {
                    Product updatedProduct = updateExistingProduct(existingProduct, request);
                    return productRepository.save(updatedProduct);
                }
        ).orElseThrow(
                ()-> new ResourceNotFoundException("Product with id: " + productId + " not found")
        );
    }

    // helper method for updateProduct
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
        // findByCategoryName is a custom method
        // it will be implemented by spring data jpa
        // findByCategoryName Name used because there is a property called category which is an object
        // or instance of Category class but we are searching by category name so we use findByCategoryName
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
