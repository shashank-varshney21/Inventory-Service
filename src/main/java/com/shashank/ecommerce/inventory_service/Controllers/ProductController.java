package com.shashank.ecommerce.inventory_service.Controllers;

import com.shashank.ecommerce.inventory_service.Dto.QuerryProductDto;
import com.shashank.ecommerce.inventory_service.Dto.ProductDto;
import com.shashank.ecommerce.inventory_service.Services.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor //This helps to get beans of all compositions/(other class instances if they're singleton or @Component), basically a dependency injection
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping()
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

    @GetMapping("/filter")
        public List<ProductDto> filter(@RequestParam String category, @RequestParam Double minPrice, @RequestParam Double maxPrice) {
            return productService.filter(category, minPrice, maxPrice);
    }

    @DeleteMapping("/{title}")
        public ResponseEntity<String> deleteProduct(@PathVariable String title) {
            return productService.deleteProduct(title);
    }

    @PostMapping()
        public ResponseEntity<String> createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PostMapping("/add")
        public ResponseEntity<String> addProduct(@RequestBody QuerryProductDto addProductDto){
            return productService.addProduct(addProductDto);
        }

    @PostMapping("/reserve")
        public ResponseEntity<String> reserveProduct(@RequestBody QuerryProductDto querryProductDto){
            return productService.reserve(querryProductDto);
        }

    @PostMapping("/release")
        public ResponseEntity<String> releaseProduct(@RequestBody QuerryProductDto querryProductDto){
        return productService.release(querryProductDto);
    }
}
