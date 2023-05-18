package com.mor.backend.services.impl;

import com.mor.backend.common.OrderStatus;
import com.mor.backend.entity.CartItem;
import com.mor.backend.entity.OrderProduct;
import com.mor.backend.entity.User;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.OrderRequest;
import com.mor.backend.repositories.CartItemRepository;
import com.mor.backend.repositories.OrderRepository;
import com.mor.backend.repositories.UserRepository;
import com.mor.backend.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderProduct createOrder(OrderRequest orderRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByIdIn(orderRequest.getCartItems());
        user.getCart().setCartTotal(user.getCart().getCartTotal() - cartItems.size());
        double total = cartItems.stream().mapToDouble(c -> c.getCartItemQuantity() + c.getProduct().getPrice()).sum();
        OrderProduct orderProduct = new OrderProduct();
        cartItems.forEach(cartItem -> {
            cartItem.setOrdered(true);
            cartItem.setOrder(orderProduct);
        });
        orderProduct.setOrderAddress(orderRequest.getOrderAddress());
        orderProduct.setCartItems(cartItems);
        orderProduct.setOrderStatus(OrderStatus.SUCCESS);
        orderProduct.setTotal(total);
        orderProduct.setUser(user);
        orderProduct.setOrderDate(LocalDate.now());
        return orderRepository.save(orderProduct);

    }

    @Override
    public void cancelOrder(long id) {
        OrderProduct orderProduct = Optional.ofNullable(orderRepository.findById(id)).orElseThrow(() ->
                new NotFoundException("Order with id : " + id + " not found"));
        orderProduct.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(orderProduct);
    }

    @Override
    public List<OrderProduct> getOrderByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return orderRepository.findByUser(user);
    }

    @Override
    public OrderProduct getOrderById(long id) {
        return Optional.ofNullable(orderRepository.findById(id)).orElseThrow(() ->
                new NotFoundException("Order with id " + id + " not found"));
    }
}



