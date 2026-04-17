package com.example.biteflow.controller;

import com.example.biteflow.dto.OrderRequest;
import com.example.biteflow.dto.OrderResponse;
import com.example.biteflow.entity.Order;
import com.example.biteflow.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest request){
        OrderResponse response = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public List<Order> getAll(){
        return orderService.getAllOrders();
    }
}
