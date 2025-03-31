package com.dailycodework.dreamshops.request;

import com.dailycodework.dreamshops.model.Category;
import lombok.Data;

@Data // here we can use data instead of getter and setter
// but in entity class we have to use getter and setter
// because of the warning, refer to the entity class for more information
// this is not direct entity in the database
public class AddProductRequest {
    private String name;
    private String brand;
    private double price;
    private int inventory;
    private String description;
    private Category category;
}
