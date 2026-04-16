package com.example.biteflow.service;

import com.example.biteflow.dto.OrderItemResponse;
import com.example.biteflow.dto.OrderRequest;
import com.example.biteflow.dto.OrderResponse;
import com.example.biteflow.dto.ProductDto;
import com.example.biteflow.entity.Order;
import com.example.biteflow.entity.OrderItem;

import com.example.biteflow.enums.OrderStatus;
import com.example.biteflow.interfaces.ProductClient;
import com.example.biteflow.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
            private final ProductClient productClient;
            private final RabbitTemplate rabbitTemplate;
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());


        List<OrderItem> items = request.productIds().stream()
                .map(productId -> {
                    ProductDto product = productClient.getProductById(productId);

                    OrderItem item = new OrderItem();
                    item.setProductId(product.id());
                    item.setQuantity(1);
                    item.setPriceAtPurchase(product.price());
                    item.setOrder(order);
                    return item;
                })
                .toList();

        order.setItems(items);
  order.setTotalAmount(items.stream().mapToDouble(OrderItem::getPriceAtPurchase).sum());
Order savedOrder = orderRepository.save(order);
OrderResponse response = mapToOrderResponse(savedOrder);
rabbitTemplate.convertAndSend("order-exchange", "order-routing-key",response);
        return response;
    }


    public Order addOrder(Order order){
       return orderRepository.save(order);
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Optional<Order> findOrderById(Long id){
        return orderRepository.findById(id);
    }
    public void deleteOrderById(Long id){
        orderRepository.deleteById(id);
    }
    private OrderResponse mapToOrderResponse(Order order){
        List<OrderItemResponse> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                )).toList();
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                itemDtos
        );
    }
}
