package com.mor.backend.controllers;

import com.mor.backend.entity.Cart;
import com.mor.backend.payload.request.CartRequest;
import com.mor.backend.payload.request.ClearCartRequest;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.services.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "demo")
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("")
    public ResponseEntity<?> getCartByUser(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Cart cart = cartService.getCartByUser(username);
        return new ResponseEntity<>(new ObjectResponse("200", "success", cart), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Cart cart = cartService.addProductToCart(cartRequest, username);
        return new ResponseEntity<>(new ObjectResponse("200", "success", cart), HttpStatus.OK);
    }

    @PostMapping("/clear")
    public ResponseEntity<?> clearProductInCart(@RequestBody ClearCartRequest clearCartRequest, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Cart cart = cartService.clearCart(username, clearCartRequest.getCartItemIds());
        return new ResponseEntity<>(new ObjectResponse("200", "success", cart), HttpStatus.OK);
    }
}
