package com.example.notification_service.service;

import com.example.notification_service.dto.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderNotificationListener {

    @RabbitListener(queues = "order-queue")
    public void handleOrderNotification(OrderResponse orderResponse){
  log.info("Otrzymano nowe zamówienie do powiadomienia!");
  log.info("ID Zamówienia: {}",orderResponse.id());
  log.info("Klient: {}",orderResponse.customerName());
  log.info("Kwota: {} PLN", orderResponse.totalAmount());
  log.info("Data: {}",orderResponse.createdAt());
    }
}
