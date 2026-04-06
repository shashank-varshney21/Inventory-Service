package com.shashank.ecommerce.inventory_service.Services;

import com.shashank.ecommerce.inventory_service.Dto.ProductDto;
import com.shashank.ecommerce.inventory_service.Dto.QuerryProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    String fetchFromOrderService();

    ResponseEntity<String> createProduct(ProductDto productDto);

    List<ProductDto> filter(String category, Double minPrice, Double maxPrice);

    ResponseEntity<String> addProduct(QuerryProductDto querryProductDto);

    ResponseEntity<String> reserve(QuerryProductDto querryProductDto);

    ResponseEntity<String> release(QuerryProductDto querryProductDto);

    ResponseEntity<String> deleteProduct(String title);
}
