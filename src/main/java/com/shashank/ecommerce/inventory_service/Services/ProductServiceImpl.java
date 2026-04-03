package com.shashank.ecommerce.inventory_service.Services;

import com.shashank.ecommerce.inventory_service.Dto.QuerryProductDto;
import com.shashank.ecommerce.inventory_service.Dto.ProductDto;
import com.shashank.ecommerce.inventory_service.Entity.ProductEntity;
import com.shashank.ecommerce.inventory_service.Repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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

    public ResponseEntity<String> createProduct(ProductDto productDto) {
        ProductEntity product = modelMapper.map(productDto, ProductEntity.class);
        productRepo.save(product);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
    }

    public List<ProductDto> filter(String category, Double minPrice, Double maxPrice) {
        List<ProductEntity> list = productRepo.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
        List<ProductDto> result = new ArrayList<>();
        for (ProductEntity p : list) {
            ProductDto productDto = modelMapper.map(p, ProductDto.class);
            result.add(productDto);
        }
        return result;
    }

    public ResponseEntity<String> addProduct(QuerryProductDto querryProductDto) {
        ProductEntity product = productRepo.findByTitle(querryProductDto.getTitle());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
        product.setStock(product.getStock() + querryProductDto.getStock());
        productRepo.save(product);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
    }

    @Transactional
    public ResponseEntity<String> reserve(QuerryProductDto querryProductDto) {
        if (querryProductDto.getStock() <= 0) {
            return ResponseEntity.badRequest().body("Invalid quantity");
        }
        try{
            ProductEntity product = productRepo.findByTitle(querryProductDto.getTitle());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found");
            }
            if (product.getStock() - querryProductDto.getStock() < 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Stock value exceeds existing value");
            }
            product.setStock(product.getStock() - querryProductDto.getStock());
            product.setReserve(product.getReserve() + querryProductDto.getStock());
            productRepo.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
        }catch(ObjectOptimisticLockingFailureException e){ //Optimistic locking exception checks here
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Product was updated by another transaction. Please retry.");
        }
    }

    @Transactional
    public ResponseEntity<String> release(QuerryProductDto querryProductDto) {
        ProductEntity product = productRepo.findByTitle(querryProductDto.getTitle());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
        try{
            Integer stock = product.getStock();
            Integer reserve = product.getReserve();
            if((stock + reserve) < querryProductDto.getStock()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("stock value exceeds existing value");
            }
            //First release Reserved Stock
            reserve -= querryProductDto.getStock();
            if(reserve<0) {
                reserve = 0;
                stock -= (querryProductDto.getStock() - reserve);
            }
            product.setStock(stock);
            product.setReserve(reserve);
            productRepo.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
        }catch(ObjectOptimisticLockingFailureException e){ //Optimistic locking exception checks here
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Product was updated by another transaction. Please retry.");
        }
    }
}
