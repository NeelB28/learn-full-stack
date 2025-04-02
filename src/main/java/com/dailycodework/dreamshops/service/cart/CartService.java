package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService{

    @Autowired
    private final CartItemService cartItemService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).
                orElseThrow(() -> new ResourceNotFoundException("cart not found with id: " + cartId));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId); // using the getCart method declared above so we dont need to handle the exception
        // everytime
        cartItemRepository.deleteAllByCartId(cartId);
        cart.getItems().clear();
        cartRepository.deleteById(cartId);
//        cart.setTotalAmount(BigDecimal.ZERO); no need to set total amount to zero because we have created
        // method to update the total amount in entity class only as service generally deals with business logic
        // and include multiple entities but logic for the method effecting only main class shoud reside in the
        // entity class only
//        cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
//        return cart.getItems().stream().map(CartItem :: getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // above is anothe shorter implementation of the below code
        return cart.getTotalAmount();
    }
}
//
//1️⃣ Why are we using getCart(cartId) instead of directly using cartRepository.findById(cartId)?
//The getCart(cartId) method is likely a wrapper around findById that:
//
//Fetches the cart safely.
//
//Throws an exception if the cart doesn’t exist (avoiding Optional.get() issues).
//
//Improves readability and reusability across the service class.
//
//Example of getCart(cartId) implementation:
//
//java
//Copy code
//public Cart getCart(Long cartId) {
//    return cartRepository.findById(cartId)
//            .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));
//}
//If we directly use findById(cartId), we’d have to manually handle the case when the cart isn’t found. Using getCart(cartId) ensures cleaner and safer code.
