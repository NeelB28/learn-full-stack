package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;

import java.math.BigDecimal;

public interface ICartItemService {
//    void addItemToCart(Long cartId, Long itemId, int quantity);
    void addItemToCart(Long cartId, Long productId, int quantity);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);

    CartItem getCartItem(Long cartId, Long productId);
}
