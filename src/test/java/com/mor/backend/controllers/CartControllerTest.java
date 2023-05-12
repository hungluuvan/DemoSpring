package com.mor.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mor.backend.common.AuthProvider;
import com.mor.backend.entity.Cart;
import com.mor.backend.entity.User;
import com.mor.backend.payload.request.CartRequest;
import com.mor.backend.payload.request.ClearCartRequest;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.services.CartService;
import com.sun.security.auth.UserPrincipal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

@ContextConfiguration(classes = {CartController.class})
@ExtendWith(SpringExtension.class)
class CartControllerTest {
    @Autowired
    private CartController cartController;

    @MockBean
    private CartService cartService;

    /**
     * Method under test: {@link CartController#getCartByUser(HttpServletRequest)}
     */
    @Test
    void testGetCartByUser() throws Exception {
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCartTotal(10.0d);
        cart.setId(1L);
        cart.setUser(new User());

        User user = new User();
        user.setAddresses(new ArrayList<>());
        user.setCart(cart);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setIsAdmin(true);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProvider(AuthProvider.local);
        user.setProviderId("42");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");

        Cart cart2 = new Cart();
        cart2.setCartItems(new ArrayList<>());
        cart2.setCartTotal(10.0d);
        cart2.setId(1L);
        cart2.setUser(user);

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setCart(cart2);
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");

        Cart cart3 = new Cart();
        cart3.setCartItems(new ArrayList<>());
        cart3.setCartTotal(10.0d);
        cart3.setId(1L);
        cart3.setUser(user2);
        when(cartService.getCartByUser(Mockito.<String>any())).thenReturn(cart3);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cart");
        requestBuilder.principal(new UserPrincipal("principal"));
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"200\",\"errorMessage\":\"success\",\"data\":{\"id\":1,\"cartTotal\":10.0,\"cartItems\":[]}}"));
    }

    /**
     * Method under test: {@link CartController#addProductToCart(CartRequest, HttpServletRequest)}
     */
    @Test
    void testAddProductToCart() throws Exception {
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCartTotal(10.0d);
        cart.setId(1L);
        cart.setUser(new User());

        User user = new User();
        user.setAddresses(new ArrayList<>());
        user.setCart(cart);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setIsAdmin(true);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProvider(AuthProvider.local);
        user.setProviderId("42");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");

        Cart cart2 = new Cart();
        cart2.setCartItems(new ArrayList<>());
        cart2.setCartTotal(10.0d);
        cart2.setId(1L);
        cart2.setUser(user);

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setCart(cart2);
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");

        Cart cart3 = new Cart();
        cart3.setCartItems(new ArrayList<>());
        cart3.setCartTotal(10.0d);
        cart3.setId(1L);
        cart3.setUser(user2);
        when(cartService.addProductToCart(Mockito.<CartRequest>any(), Mockito.<String>any())).thenReturn(cart3);
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/v1/cart");
        postResult.principal(new UserPrincipal("principal"));

        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(1L);
        cartRequest.setQuantity(1);
        String content = (new ObjectMapper()).writeValueAsString(cartRequest);
        MockHttpServletRequestBuilder requestBuilder = postResult.contentType(MediaType.APPLICATION_JSON).content(content);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"200\",\"errorMessage\":\"success\",\"data\":{\"id\":1,\"cartTotal\":10.0,\"cartItems\":[]}}"));
    }

    /**
     * Method under test: {@link CartController#clearProductInCart(ClearCartRequest, HttpServletRequest)}
     */
    @Test
    void testClearProductInCart() {
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCartTotal(10.0d);
        cart.setId(1L);
        cart.setUser(new User());

        User user = new User();
        user.setAddresses(new ArrayList<>());
        user.setCart(cart);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setIsAdmin(true);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProvider(AuthProvider.local);
        user.setProviderId("42");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");

        Cart cart2 = new Cart();
        cart2.setCartItems(new ArrayList<>());
        cart2.setCartTotal(10.0d);
        cart2.setId(1L);
        cart2.setUser(user);

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setCart(cart2);
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");

        Cart cart3 = new Cart();
        cart3.setCartItems(new ArrayList<>());
        cart3.setCartTotal(10.0d);
        cart3.setId(1L);
        cart3.setUser(user2);
        CartService cartService = mock(CartService.class);
        when(cartService.clearCart(Mockito.<String>any(), Mockito.<List<Long>>any())).thenReturn(cart3);
        CartController cartController = new CartController(cartService);
        ClearCartRequest clearCartRequest = new ClearCartRequest();
        DefaultMultipartHttpServletRequest request = mock(DefaultMultipartHttpServletRequest.class);
        when(request.getUserPrincipal()).thenReturn(new UserPrincipal("userPrincipal"));
        ResponseEntity<?> actualClearProductInCartResult = cartController.clearProductInCart(clearCartRequest, request);
        assertTrue(actualClearProductInCartResult.hasBody());
        assertTrue(actualClearProductInCartResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualClearProductInCartResult.getStatusCode());
        assertSame(cart3, ((ObjectResponse) actualClearProductInCartResult.getBody()).getData());
        assertEquals("200", ((ObjectResponse) actualClearProductInCartResult.getBody()).getErrorCode());
        assertEquals("success", ((ObjectResponse) actualClearProductInCartResult.getBody()).getErrorMessage());
        verify(cartService).clearCart(Mockito.<String>any(), Mockito.<List<Long>>any());
        verify(request).getUserPrincipal();
    }
}

