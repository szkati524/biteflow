package com.example.audit_service.service;

import com.example.audit_service.entity.AuditLog;
import com.example.audit_service.entity.Order;
import com.example.audit_service.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditConsumer {

    private final AuditRepository auditRepository;
@KafkaListener(topics= "order-audit-topic", groupId = "audit-group")
    public void handleOrderAudit(Order order){
        log.info("Odebrano zamówienie do audytu {} ",order.getOrderNumber());
        AuditLog logEntry = new AuditLog();
        logEntry.setOrderNumber(order.getOrderNumber());
        logEntry.setCustomerName(order.getCustomerName());
        logEntry.setLocalDateTime(LocalDateTime.now());
        auditRepository.save(logEntry);

    }
}
