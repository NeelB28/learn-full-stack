package com.dailycodework.dreamshops.request;

import com.dailycodework.dreamshops.model.Category;
import lombok.Data;

@Data
public class ProductUpdateRequest {
    private String name;
    private String brand;
    private double price;
    private int inventory;
    private String description;
    private Category category;
}
