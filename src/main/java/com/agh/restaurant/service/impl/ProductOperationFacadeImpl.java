package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.ProductStatus;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.dao.FoodRepository;
import com.agh.restaurant.domain.dao.ProductRepository;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.service.ProductOperationFacade;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOperationFacadeImpl implements ProductOperationFacade {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    FoodRepository foodRepository;

    @Override
    public FoodEntity addMenuItem(RestaurantMenuItem menuItem) {
        System.out.println(menuItem);
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setName(menuItem.getName());
        foodEntity.setDishOrDrink(menuItem.getFoodType());
        foodEntity.setPrice(menuItem.getPrice());
        foodEntity.setNeededProducts(menuItem.getItemsNeededNames().stream().map(
                itemName -> productRepository.findByName(itemName)
        ).collect(Collectors.toList()));
        if (foodEntity.getNeededProducts().contains(null)){
            throw new IllegalArgumentException("Some products are not defined in storage.");
        }
//        foodEntity.getNeededProducts().forEach(product -> {
//            ProductEntity productEntity = productRepository.findByName(product.getName());
//            productEntity.getUsedInFoods().add(foodEntity);
//            productRepository.save(productEntity);
//        });
        foodEntity.setAvailable(foodEntity.getNeededProducts().stream().map(ProductEntity::getProductStatus)
                .allMatch(x -> x != ProductStatus.NOT_AVAILABLE));
        return foodRepository.save(foodEntity);
    }

    @Override
    public ProductEntity addProductItem(ProductItem productItem) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setAmount(productItem.getAmount());
        productEntity.setName(productItem.getName());
        if (productItem.getAmount() > 0) {
            if (productItem.getAmount() > 10) {
                productEntity.setProductStatus(ProductStatus.AVAILABLE);
            } else {
                productEntity.setProductStatus(ProductStatus.LOW);
            }
        } else {
            productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);
        }
        return productRepository.save(productEntity);
    }

    @Override
    public ArrayList<ProductEntity> alterProductAmount(List<ProductItem> productItemList) {
        productItemList.forEach(productItem -> {
            ProductEntity productEntity = productRepository.findByName(productItem.getName());
            productEntity.setAmount(productEntity.getAmount() + productItem.getAmount());
            if (productItem.getAmount() > 0) {
                if (productItem.getAmount() > 10) {
                    productEntity.setProductStatus(ProductStatus.AVAILABLE);
                } else {
                    productEntity.setProductStatus(ProductStatus.LOW);
                }
            } else {
                productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);
            }
            productRepository.save(productEntity);
        });
        foodRepository.findAll().forEach(foodEntity -> {
            if (!foodEntity.getAvailable()) {
                foodEntity.setAvailable(foodEntity.getNeededProducts().stream()
                        .noneMatch(item -> item.getProductStatus().equals(ProductStatus.NOT_AVAILABLE)));
            }
        });
        return Lists.newArrayList(productRepository.findAll());
    }

    @Override
    public List<String> getRequestedItems() {
        return Lists.newArrayList(productRepository.findAll()).stream().map(ProductEntity::getName).collect(Collectors.toList());
    }

    @Override public List<ProductEntity> getProducts() {
        return Lists.newArrayList(productRepository.findAll());
    }
}
