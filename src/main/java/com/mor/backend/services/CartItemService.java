package com.mor.backend.services;

import com.mor.backend.entity.CartItem;
import com.mor.backend.payload.request.CartRequest;

public interface CartItemService {
    void deleteCartItemById(long id);

    CartItem createCartItem(CartRequest cartRequest);
//    void updateCartItem(CartRequest cartRequest,String username);
}
