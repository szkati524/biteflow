package com.biteflow.productservice.controller;

import com.biteflow.productservice.entity.Product;
import com.biteflow.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }


}
