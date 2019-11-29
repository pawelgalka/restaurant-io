package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByName(String name);
}
