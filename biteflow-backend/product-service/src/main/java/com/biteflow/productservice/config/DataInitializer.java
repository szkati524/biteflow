package com.biteflow.productservice.config;

import com.biteflow.productservice.entity.Product;
import com.biteflow.productservice.repository.ProductRepository;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(ProductRepository productRepository){
        return args -> {
            System.out.println("--- Inicjalizacja danych testowych ---");

            productRepository.save(new Product(null, "Pizza Margarita","pizza_margarita", 32.00));
            productRepository.save(new Product(null, "Pizza Pepperoni","pizza_pepperoni", 38.50));
            productRepository.save(new Product(null, "Burger Wołowy","burger_wolowy" ,29.00));
            productRepository.save(new Product(null, "Coca Cola","coca_cola", 7.00));

            System.out.println("--- Produkty dodane do bazy H2 ---");
        };
    }
}
