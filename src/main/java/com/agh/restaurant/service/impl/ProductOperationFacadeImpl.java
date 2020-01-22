/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.ProductStatus;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.dao.FoodRepository;
import com.agh.restaurant.domain.dao.ProductRepository;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.service.ProductOperationFacade;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setName(menuItem.getName());
        foodEntity.setDishOrDrink(menuItem.getFoodType());
        foodEntity.setPrice(menuItem.getPrice());
        foodEntity.setNeededProducts(menuItem.getItemsNeededNames().stream().map(
                itemName -> productRepository.findByName(itemName)
        ).collect(Collectors.toList()));
        if (foodEntity.getNeededProducts().contains(null)) {
            throw new IllegalArgumentException("Some products are not defined in storage.");
        }
        foodEntity.setAvailable(foodEntity.getNeededProducts().stream().map(ProductEntity::getProductStatus)
                .allMatch(x -> x != ProductStatus.NOT_AVAILABLE));
        return foodRepository.save(foodEntity);
    }

    @Override
    public ProductEntity addProductItem(ProductItem productItem) {
        ProductEntity productEntity = productRepository.findByName(productItem.getName());
        if (productEntity == null) {
            productEntity = new ProductEntity();
            productEntity.setName(productItem.getName());
            productEntity.setAmount(productItem.getAmount());
        } else {
            productEntity.setAmount(productItem.getAmount() + productEntity.getAmount());
        }
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

    @Override public List<FoodEntity> getMenuList() {
        return Lists.newArrayList(foodRepository.findAll());
    }

    @Override
    public List<ProductEntity> alterProductAmount(List<ProductItem> productItemList) {
        productItemList.forEach(productItem -> {
            ProductEntity productEntity = productRepository.findByName(productItem.getName());
            if (productItem.getAmount() < 0) {
                throw new IllegalArgumentException("Cannot subtract from storage");
            }
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
            if (Boolean.FALSE.equals(foodEntity.getAvailable())) {
                foodEntity.setAvailable(foodEntity.getNeededProducts().stream()
                        .noneMatch(item -> item.getProductStatus().equals(ProductStatus.NOT_AVAILABLE)));
                foodRepository.save(foodEntity);
            }
        });
        return Lists.newArrayList(productRepository.findAll());
    }

    @Override
    public List<String> getRequestedItems() {
        return Lists.newArrayList(productRepository.findAll()).stream()
                .filter(productEntity -> !ProductStatus.AVAILABLE.equals(productEntity.getProductStatus()))
                .map(ProductEntity::getName).collect(Collectors.toList());
    }

    @Override public List<ProductEntity> getProducts() {
        return Lists.newArrayList(productRepository.findAll());
    }

    @Override public void deleteMenuItem(Long menuItem) {
        foodRepository.deleteById(menuItem);
    }
}
