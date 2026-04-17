package com.example.biteflow.service;

import com.example.biteflow.dto.*;
import com.example.biteflow.entity.Order;
import com.example.biteflow.entity.OrderItem;

import com.example.biteflow.enums.OrderStatus;
import com.example.biteflow.interfaces.ProductClient;
import com.example.biteflow.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
            private final ProductClient productClient;
            private final RabbitTemplate rabbitTemplate;
            private final WebClient.Builder webClientBuilder;
    @Transactional
    @CircuitBreaker(name = "productService",fallbackMethod = "fallbackProductInfo")
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        log.info("Składanie zamówienia dla: {}", orderRequest.customerName());


        List<String> skuCodes = orderRequest.skuCodes();

        List<ProductInfoResponse> productInfos = webClientBuilder.build().get()
                .uri("http://product-service/api/products/info",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToFlux(ProductInfoResponse.class)
                .collectList()
                .block();


        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (!allInStock) throw new IllegalArgumentException("Brak towaru!");


        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCustomerName(orderRequest.customerName());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());


        List<OrderItem> orderItems = skuCodes.stream()
                .map(sku -> {

                    ProductInfoResponse pInfo = productInfos.stream()
                            .filter(p -> p.skuCode().equals(sku))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Nie znaleziono produktu: " + sku));

                    OrderItem item = new OrderItem();
                    item.setProductId(pInfo.productId());
                    item.setPriceAtPurchase(pInfo.price());
                    item.setQuantity(1);
                    item.setOrder(order);
                    return item;
                }).toList();


        order.setItems(orderItems);


        double total = orderItems.stream()
                .mapToDouble(i -> i.getPriceAtPurchase() * i.getQuantity())
                .sum();
        order.setTotalAmount(total);


        orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent(order.getId(),orderRequest.skuCodes());
        rabbitTemplate.convertAndSend("orderQueue",event);
        log.info("Wysłano zdarzenie zamówienia do RabbitMQ dla magazynu");

        OrderResponse response = mapToOrderResponse(order);

        rabbitTemplate.convertAndSend("order-exchange", "order-routing-key", response);
        return response;
    }
public OrderResponse fallbackProductInfo(OrderRequest orderRequest,RuntimeException runtimeException){
log.error("Circuit Breaker zadziałał! Product Service jest niedostępny. Powód: {}",runtimeException.getMessage());

throw new RuntimeException("Przepraszamy,usługa katalogu produktów jest chwilowo niedostępna. Spróbuj pózniej!");
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
