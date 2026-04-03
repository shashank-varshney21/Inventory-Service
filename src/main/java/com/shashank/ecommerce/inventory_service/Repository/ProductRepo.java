package com.shashank.ecommerce.inventory_service.Repository;

import com.shashank.ecommerce.inventory_service.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryAndPriceBetween(
            String category,
            Double minPrice,
            Double maxPrice
    );

    ProductEntity findByTitle(
            String title
    );
}
