package com.shashank.ecommerce.inventory_service.Controllers;

import com.shashank.ecommerce.inventory_service.Dto.ProductDto;
import com.shashank.ecommerce.inventory_service.Services.ProductService;
import com.shashank.ecommerce.inventory_service.Services.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor //This helps to get beans of all compositions/(other class instances), basically a dependency injection
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Fetching all products via controller");
        List<ProductDto> inventories = productService.getAllProducts();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.info("Fetching product by id via controller");
        ProductDto inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/helloInventory")
        public String helloInventory() {
        return "Hello from Inventory Service!";
    }

    @GetMapping("/fetchOrder")
        public String fetchFromOrderService() {
            return productService.fetchFromOrderService();
    }
}
