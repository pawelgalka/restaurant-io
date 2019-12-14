package com.agh.restaurant.service;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public interface ProductOperationFacade {
    FoodEntity addMenuItem(RestaurantMenuItem menuItem);

    ProductEntity addProductItem(ProductItem productItem);

    ArrayList<ProductEntity> alterProductAmount(List<ProductItem> productItemList);

    List<String> getRequestedItems();

    List<ProductEntity> getProducts();

    void deleteMenuItem(Long menuItem);
}
