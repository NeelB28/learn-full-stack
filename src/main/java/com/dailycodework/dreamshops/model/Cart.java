package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO; // Initialize to zero
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;


    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this); // item.setCart(this) is from the CartItem class which has Cart as a field
        updateTotalAmount();
    }
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }
    public void updateTotalAmount() {
        this.totalAmount = items.stream().map(
                item->{
                    BigDecimal unitPrice = item.getUnitPrice();
                    if(unitPrice == null) {
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                }
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

//### Why Logic is in the Entity Class?
//Adding methods like `addItem`, `removeItem`, and `updateTotalAmount` in the **Cart model** instead of the service class follows the **principles of object-oriented programming (OOP)**, especially **encapsulation and domain-driven design (DDD)**. Here’s why:
//
//        ### 1. **Encapsulation (Keeping Behavior Close to Data)**
//        - The `Cart` class represents an entity that encapsulates both **data (fields)** and **behavior (methods)** related to managing the cart.
//   - The logic of adding/removing items and updating the total amount naturally **belongs to the Cart itself**, so it makes sense to define these methods within the model.
//   - This way, the integrity of the `Cart` object is maintained within its own class.
//
//        ### 2. **Domain-Driven Design (DDD) Approach**
//        - According to **DDD**, entities should contain business logic that **directly affects their state** rather than delegating everything to a service class.
//        - `Cart` is an **aggregate root** in this case, meaning it should enforce consistency for its child objects (`CartItem`).
//        - Methods like `addItem`, `removeItem`, and `updateTotalAmount` are **directly related to the Cart’s business rules**, making them a good fit for the entity class.
//
//        ### 3. **Ensuring Data Consistency**
//        - If this logic were in the service class, other parts of the code might modify `Cart` and `CartItem` objects **without updating the total amount**.
//        - By placing these methods in the entity class, we ensure that **whenever an item is added/removed, the total amount is always updated**.
//
//        ### 4. **Service Layer Focuses on Business Workflow, Not Entity Behavior**
//        - The **Service layer** should focus on coordinating operations **between multiple entities** or handling **transactions**, not on modifying individual entity states.
//        - A service method might call `cart.addItem(cartItem)`, but the logic of **how** an item is added and how the total amount is updated should reside in the `Cart` entity.
//
//### 5. **Better Code Reusability & Maintainability**
//        - If this logic were in the service class, you’d need to manually update the total amount every time you modify the cart elsewhere.
//   - Placing it inside the model ensures that **whenever the Cart object is modified, the update happens automatically**.
//
//        ---
//
//        ### When to Put Logic in the Service Class?
//While putting logic in the entity class is beneficial, there are cases where logic **should be in the service layer**, such as:
//        - **Complex business workflows** that involve multiple entities (e.g., applying discounts, handling checkout processes).
//        - **External dependencies** like calling a payment API.
//- **Operations that don’t belong to a single entity**, like fetching all carts of a user.
//
//---
//
//        ### Summary:
//        - **Entity class** should handle behavior related to **its own state** (like adding/removing items).
//        - **Service class** should manage **higher-level business workflows** involving multiple entities.
//- Keeping logic inside the entity improves **data integrity, encapsulation, and maintainability**.
//
//Would you like to restructure any part of this, or do you need further clarification? 🚀