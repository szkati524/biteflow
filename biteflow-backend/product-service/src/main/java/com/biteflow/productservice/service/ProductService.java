package com.biteflow.productservice.service;

import com.biteflow.productservice.dto.ProductInfoResponse;
import com.biteflow.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductInfoResponse> getProductsBySkuCodes(List<String> skuCodes){
        return productRepository.findBySkuCodeIn(skuCodes).stream()
                .map(product -> new ProductInfoResponse(
                        product.getSkuCode(),
                        product.getId(),
                        product.getPrice()
                ))
                .toList();
    }
}
