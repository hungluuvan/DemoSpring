package com.mor.backend.services.impl;

import com.mor.backend.entity.CartItem;
import com.mor.backend.entity.Product;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.CartRequest;
import com.mor.backend.repositories.CartItemRepository;
import com.mor.backend.repositories.CartRepository;
import com.mor.backend.repositories.ProductRepository;
import com.mor.backend.services.CartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public void deleteCartItemById(long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItem createCartItem(CartRequest cartRequest) {
        Product existingProduct = Optional.ofNullable(productRepository.findById(cartRequest.getProductId())).orElseThrow(
                () -> new NotFoundException("Product not found ")
        );
        CartItem cartItem = new CartItem();
        cartItem.setProduct(existingProduct);
        cartItem.setCartItemQuantity(1);
        return cartItem;
    }

//    @Override
//    public void updateCartItem(CartRequest cartRequest) {
//        return null
//    }
}
