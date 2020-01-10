package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.service.ProductOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SupplierApi {

    @Autowired
    private ProductOperationFacade productOperationFacade;

    @PostMapping(value = "/api/supplier/supply")
    public ResponseEntity supplyRestaurant(@RequestBody List<ProductItem> list){
        try {
            return ResponseEntity.ok().body(productOperationFacade.alterProductAmount(list));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping(value = "/api/supplier/requestedItems")
    public List<String> getRequestedItems(){
        return productOperationFacade.getRequestedItems();
    }

    @GetMapping(value = "/api/supplier/products")
    public List<ProductEntity> getItems(){
        return productOperationFacade.getProducts();
    }
}

