package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.service.ProductOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_SUPPLIER})
public class SupplierApi {

    @Autowired
    private ProductOperationFacade productOperationFacade;

    @PostMapping(value = "/api/supplier/supply")
    public void supplyRestaurant(@RequestBody List<ProductItem> list){
        productOperationFacade.alterProductAmount(list);
    }

    @GetMapping(value = "/api/supplier/requestedItems")
    public List<String> getRequestedItems(){
        return productOperationFacade.getRequestedItems();
    }
}

