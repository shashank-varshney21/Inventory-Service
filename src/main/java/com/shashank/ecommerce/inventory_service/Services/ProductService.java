package com.shashank.ecommerce.inventory_service.Services;

import com.shashank.ecommerce.inventory_service.Dto.ProductDto;

import java.util.List;

public interface ProductService {
    public List<ProductDto> getAllProducts();

    public ProductDto getProductById(Long id);

    public String fetchFromOrderService();
}
