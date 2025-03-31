package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @Autowired
    private final IProductService productService;

    // Add your product-related endpoints here
    // For example: @GetMapping("/all"), @DeleteMapping etc..

    // get all products
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProdcucts() {
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Success", productList));
    }

    // get product by id
    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
//        return Optional.ofNullable(productService.getProductById(id))
//                .map(product -> ResponseEntity.ok(new ApiResponse("Success", product)))
//                .orElse(ResponseEntity.notFound().build());
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Success", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null ));
        }
//        if (product != null) {
//            return ResponseEntity.ok(new ApiResponse("Success", product));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
        // NO NEED FOR OPTIONAL OR IF-ELSE COZ EXCEPTION ALREADY BEEN ADDED IN THE SERVICE
    }

    // add the product, using @RequestBody coz product will be added by user so developer java will request the data
    // from the user
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product added successfully", newProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
        // no need to find the product by id coz it will be added by the user
        // so not_found error no implied instead we are using internal server error

    }

    // update the product
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Long id, @RequestBody ProductUpdateRequest product) {
        // purpose of try and catch is that we have added an exception in the updateProduct method of productService
        // class and if it is not found then it will throw the exception so we have to handle it here using cath, which will
        // catch the exception and return the response
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", updatedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // delete the product
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
//
//        return Optional.ofNullable(productService.getProductById(id))
//                .map(product -> {
//                    productService.deleteProductById(id);
//                    return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
//                })
//                .orElse(ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null)));
//        Dont use this use try cath instead coz for null stuff the exception is already included in the service class
//        So instead use the try catch block where catch will catch the exception

    // get products by brand and name
    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
//        try {
//            List<Product> products = productService.getProductsByBrandAndName(brand, name);
//            return ResponseEntity.ok(new ApiResponse("Success", products));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        }
        // now here we need to add if-else coz service is not checking null exception so either use if-else or optional.ofNullable
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            if (products != null) {
                return ResponseEntity.ok(new ApiResponse("Success", products));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
        // here we are using only exception general class, because in service we have not an exception for
        // resourcenotfound so how will it catch it so that is why we are using general exception
        // in crud ops we have handled the exception in the service class in the name of resourcenotfound
        // so we can catch it over here
    }

    // get products by category and brand
    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            return Optional.ofNullable(productService.getProductsByCategoryAndBrand(category, brand))
                    .map(products -> ResponseEntity.ok(new ApiResponse("Success", products)))
                    .orElse(ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // get products by name
    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            return Optional.ofNullable(productService.getProductsByName(name))
                    .map(products -> ResponseEntity.ok(new ApiResponse("Success", products)))
                    .orElse(ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // get products by brand
    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            return Optional.ofNullable(productService.getProductsByBrand(brand))
                    .map(products -> ResponseEntity.ok(new ApiResponse("Success", products)))
                    .orElse(ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // get products by category
    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            return Optional.ofNullable(productService.getProductsByCategory(category))
                    .map(products -> ResponseEntity.ok(new ApiResponse("Success", products)))
                    .orElse(ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // count products by brand and name
    @GetMapping("/products/count/by/brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            if(productService.getProductsByBrandAndName(brand, name) == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Brand or name not found", null));
            }
            var count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Success", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
