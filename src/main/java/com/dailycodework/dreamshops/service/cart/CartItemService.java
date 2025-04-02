package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartItemService implements ICartItemService{

    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService; // no need to import CartService because in same package and it is public
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Get the cart by cartId
        // 2. Get the product by productId
        // 3. Check if the product is already in the cart
        // 4. If it is, then increase the quantity with the requested quantity
        // 5. If it is not, then create a new CartItem entry and add it to the cart
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        // Check if the product is already in the cart
        CartItem cartItem = cart.getItems().stream().
                filter(item -> item.getProduct().getId().equals(productId)).
                findFirst().
                orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        // after setting everything in cart now u need to
        // add items to the cart too
        // and then save both cartItem and cart also
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream().
                filter(item->item.getProduct().getId().equals(productId)).
                findFirst().
                ifPresent(item-> {
                        item.setQuantity(quantity);
                        item.setUnitPrice(item.getProduct().getPrice());
                        item.setTotalPrice();

                    }
                );
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart); // cartItem is in the cart therefore
        // automatically it will get saved
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().
                stream().
                filter(item -> item.getProduct().getId().equals(productId)).
                findFirst().
                orElseThrow(()-> new ResourceNotFoundException("Item Not Found"));
    }
}
