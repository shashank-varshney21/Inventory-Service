package com.shashank.ecommerce.inventory_service.Dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ProductDto {
    @Id
    private Long id;
    private String title;
    private String category;
    private Double price;
    private Integer stock;
    private Integer reserve;
}
