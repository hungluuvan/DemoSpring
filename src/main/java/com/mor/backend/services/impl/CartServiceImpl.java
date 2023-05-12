package com.mor.backend.services.impl;

import com.mor.backend.entity.Cart;
import com.mor.backend.entity.CartItem;
import com.mor.backend.entity.User;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.CartRequest;
import com.mor.backend.repositories.CartRepository;
import com.mor.backend.repositories.UserRepository;
import com.mor.backend.services.CartItemService;
import com.mor.backend.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    @Override
    public Cart getCartByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        Cart cart;
        if (user.getCart() == null) {
            createCartWithCurrentUser(user);
            cart = cartRepository.getCartByUser(user);
        } else {
            cart = user.getCart();
        }
        List<CartItem> cartItems = cart.getCartItems().stream().filter(c -> !c.isOrdered()).collect(Collectors.toList());
        cart.setCartItems(cartItems);
        return cart;
    }


    @Override
    public void createCartWithCurrentUser(User user) {
        Cart cart = new Cart();
        cart.setCartTotal();
        user.setCart(cart);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public Cart addProductToCart(CartRequest cartRequest, String username) {
        Cart cart = getCartByUser(username);
        CartItem cartItem = cartItemService.createCartItem(cartRequest);
        cartItem.setCart(cart);
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.size() == 0) {
            cartItems = new ArrayList<>();
            cartItems.add(cartItem);
        } else {
            boolean flag = false;
            for (CartItem item : cartItems
            ) {
                if (item.getProduct().getId() == cartRequest.getProductId() && !item.isOrdered()) {
                    item.setCartItemQuantity(item.getCartItemQuantity() + cartRequest.getQuantity());
                    flag = true;
                }
            }
            if (!flag) {
                cartItems.add(cartItem);
            }

        }
        cart.setCartTotal(cartItems.size());
        cart.setCartItems(cartItems);
        return cartRepository.save(cart);
    }

    @Override
    public Cart clearCart(String username, List<Long> cartItemIds) {
        Cart cart = getCartByUser(username);
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.stream().allMatch(CartItem::isOrdered)) {
            return cart;
        }
        for (CartItem c : cartItems
        ) {
            for (Long id : cartItemIds) {
                if (id.equals(c.getId())) {
                    c.setOrdered(true);
                }
            }
        }
        cart.setCartItems(cartItems);
        cart.setCartTotal(cartItems.stream().filter(c -> !c.isOrdered()).count());
        return cartRepository.save(cart);

    }


}