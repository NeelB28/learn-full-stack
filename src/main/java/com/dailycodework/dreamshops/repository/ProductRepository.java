package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category); // custom method

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String brand);

    List<Product> findByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name);
    // so the rule of custom method is that you have to use findBy or countBy
    // then the property name of the entity class on which you want to search cant use non-existing property/
    // entity class property
    // if you want to search by multiple properties then you can use And or Or
    // then the property name of the entity class on which you want to search
    // you can also use other keywords like Between, LessThan, GreaterThan, Like, etc
    // you can also use OrderBy to order the results
    // you can also use Top or First to get the top or first results
    // you can also use Distinct to get the distinct results
    // but custom methods are limited to some extent
    // if you want to write a complex query then you can use @Query annotation
    // and write the query in JPQL or native SQL
    // and you can also use @Param annotation to pass the parameters
    // and you can also use @Modifying annotation to update or delete the records
    // but can not use @Modifying with @Query annotation that returns the result
}
