package com.agh.restaurant.service;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.model.ProductEntity;

import java.util.List;

public interface ProductOperationFacade {
    void addMenuItem(RestaurantMenuItem menuItem);

    void addProductItem(ProductItem productItem);

    void alterProductAmount(List<ProductItem> productItemList);

    List<String> getRequestedItems();
}
