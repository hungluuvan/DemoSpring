package com.mor.backend.services;

import com.mor.backend.entity.Cart;
import com.mor.backend.entity.User;
import com.mor.backend.payload.request.CartRequest;

import java.util.List;

public interface CartService {
    Cart getCartByUser(String username);

    void createCartWithCurrentUser(User user);

    Cart addProductToCart(CartRequest cartRequest, String user);

    Cart clearCart(String username, List<Long> carItemIds);

}
