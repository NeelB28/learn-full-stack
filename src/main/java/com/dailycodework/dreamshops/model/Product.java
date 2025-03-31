package com.dailycodework.dreamshops.model;
// @AllArgsConstructor removed as we are using custom constructor
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private double price;
    private int inventory;
    private String description;

    // Many products can belong to one category
    // Removed cascade type as categories should exist independently of products
    @ManyToOne
    @JoinColumn(name = "category_id") // name of the foreign key column join on fk category_id
    private Category category;

    // One product can have many images
    // Cascade type is used to propagate the effect of an operation to the associated entities
    // Orphan removal is used to remove the child entities when they are no longer referenced by the parent entity
    // mappedBy attribute is used to specify the entity that owns the relationship
    // in simple words, the entity that has the foreign key column and here it is Image entity
    // and mappedBy = "product" means that the product field in the Image entity is the owning side of the relationship
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Product(String name, String brand, double price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }
}
