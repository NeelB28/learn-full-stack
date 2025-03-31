package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    // Lob annotation is used to store large objects in the database
    @Lob
    private Blob image;
    private String downloadUrl;

    // Many images can belong to one product
    @ManyToOne
    // Join the id column in the image table to the id column in the product table and name the column product_id
    // as we need to provide name to foreign key column
    @JoinColumn(name = "product_id")
    private Product product;
}

