package com.shashank.ecommerce.inventory_service.Repository;

import com.shashank.ecommerce.inventory_service.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
}
