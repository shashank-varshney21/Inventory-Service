package com.shashank.ecommerce.inventory_service.Services;

import com.shashank.ecommerce.inventory_service.Dto.ProductDto;
import com.shashank.ecommerce.inventory_service.Entity.ProductEntity;
import com.shashank.ecommerce.inventory_service.Repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl {

    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    public List<ProductDto> getAllProducts() {
        log.info("Fetching all inventory items");
        List<ProductEntity> inventories = productRepo.findAll();
        return inventories.stream().
                map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
    }

    public ProductDto getProductById(Long id) {
        log.info("Fetching Product with Id: {}",id);
        Optional<ProductEntity> inventory = productRepo.findById(id);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(()-> new RuntimeException("Inventory not found"));
    }

    public String fetchFromOrderService() {
        ServiceInstance orderService = discoveryClient.getInstances("order-service").get(0);
        String res= restClient.get()
                .uri(orderService.getUri()+"/api/v1/orders/hello")
                .retrieve()
                .body(String.class);
        System.out.println(res);
        return "SUCCESS";
    }
}
