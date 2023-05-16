package com.mor.backend.services;

import com.mor.backend.entity.OrderProduct;
import com.mor.backend.payload.request.OrderRequest;

import java.util.List;

public interface OrderService {
    OrderProduct createOrder(OrderRequest orderRequest, String username );
    void cancelOrder(long id );
    List<OrderProduct> getOrderByUser(String username);
    OrderProduct getOrderById(long id);

}
