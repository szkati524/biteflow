package com.biteflow.productservice.controller;

import com.biteflow.productservice.dto.ProductInfoResponse;
import com.biteflow.productservice.entity.Product;
import com.biteflow.productservice.repository.ProductRepository;
import com.biteflow.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    @GetMapping("/info")
    public List<ProductInfoResponse> getProductInfo(@RequestParam("skuCode") List<String> skuCode){
        return productService.getProductsBySkuCodes(skuCode);
    }
    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }


}
