package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);
}
