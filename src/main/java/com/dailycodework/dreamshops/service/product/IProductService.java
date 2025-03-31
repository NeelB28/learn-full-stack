package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request); // C
    Product getProductById(Long productId); // R
    Product updateProduct(ProductUpdateRequest request, Long productId); // U
    void deleteProductById(Long productId); // D

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    // List<Product> getProductsBySearch(String search); same as below
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    // List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);
    // List<Product> getProductsByPriceRangeAndCategory(Double minPrice, Double maxPrice, Long categoryId);

    Long countProductsByBrandAndName(String brand, String name);
}
