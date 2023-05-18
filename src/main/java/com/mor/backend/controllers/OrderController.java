package com.mor.backend.controllers;

import com.mor.backend.common.Message;
import com.mor.backend.entity.OrderProduct;
import com.mor.backend.payload.request.OrderRequest;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "demo")
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ObjectResponse> getOrderByUser(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        List<OrderProduct> orderProducts = orderService.getOrderByUser(username);
        if (!orderProducts.isEmpty()) {
            return new ResponseEntity<>(new ObjectResponse("200", Message.SUCCESS, orderProducts), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ObjectResponse("204", Message.SUCCESS, orderProducts), HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping("")
    public ResponseEntity<ObjectResponse> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        OrderProduct orderProduct = orderService.createOrder(orderRequest, username);
        return new ResponseEntity<>(new ObjectResponse("200", (HttpStatus.OK).toString(), orderProduct), HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ObjectResponse> cancelOrder(@PathVariable long id) {
        orderService.cancelOrder(id);
        return new ResponseEntity<>(new ObjectResponse("200", (HttpStatus.OK).toString(), null), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectResponse> getOrderById(@PathVariable long id) {
        OrderProduct orderProduct = orderService.getOrderById(id);
        return new ResponseEntity<>(new ObjectResponse("200", (HttpStatus.OK).toString(), orderProduct), HttpStatus.OK);
    }

}
