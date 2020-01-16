package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.FoodResponse;
import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.facade.FeedbackService;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.domain.model.RaportEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.ProductOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.agh.restaurant.web.facade.WebFacade;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ManagerApi {

    private final WebFacade webFacade;

    private final FeedbackService feedbackService;

    private final TableOperationFacade tableOperationFacade;

    private final ProductOperationFacade productOperationFacade;


    public ManagerApi(WebFacade webFacade, FeedbackService feedbackService, TableOperationFacade tableOperationFacade,
            ProductOperationFacade productOperationFacade) {
        this.webFacade = webFacade;
        this.feedbackService = feedbackService;
        this.tableOperationFacade = tableOperationFacade;
        this.productOperationFacade = productOperationFacade;
    }

    @PostMapping(value = "/api/management/signup")
    public ResponseEntity signUp(@RequestBody RegisterUserInit registrationUnit) {
        try {
            webFacade.registerUser(registrationUnit);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
        return ResponseEntity.ok().body("");
    }

    @PatchMapping(value = "/api/management/update")
    public ResponseEntity updateUser(@RequestBody RegisterUserInit registrationUnit) {
        try {
            webFacade.updateUser(registrationUnit);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping(value = "/api/management/deleteUserId/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        try {
            webFacade.deleteUserById(id);

        }  catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).body("");
        }
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping(value = "/api/management/deleteUserName/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        try {
            webFacade.deleteUserByUsername(username);

        }  catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).body("");
        }
        return ResponseEntity.ok().body("");
    }

    @GetMapping(value = "/api/management/fetchUsers")
    public List<UserEntity> getUsers(){
        return webFacade.getUsers();

    }

    @PostMapping(value = "/api/management/addTable")
    public void addTable(){
        tableOperationFacade.createTable();
    }

    @PostMapping(value = "/api/management/addTables/{num}")
    public void addTables(@PathVariable("num") Integer numberOfTables){
        for (int i = 0; i<numberOfTables; i++){
            this.addTable();
        }
    }

    @PostMapping(value = "/api/management/addMenuItem")
    public ResponseEntity<FoodEntity> addMenuItem(@RequestBody RestaurantMenuItem menuItem){
        try {
            return ResponseEntity.ok(productOperationFacade.addMenuItem(menuItem));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping(value = "/api/management/deleteMenuItem/{id}")
    public void deleteMenuItem(@PathVariable("id") Long menuItem){
        productOperationFacade.deleteMenuItem(menuItem);
    }

    @GetMapping(value = "/api/management/getMenu")
    public Map<FoodEntity.FoodType, List<FoodResponse>> getMenu(){
        return productOperationFacade.getMenuList().stream().map(FoodResponse::new).collect(Collectors.groupingBy(
                FoodResponse::getDishOrDrink));
    }

    @PostMapping(value = "/api/management/addProductItem")
    public void addProductItem(@RequestBody ProductItem productItem){
        productOperationFacade.addProductItem(productItem);
    }

    @GetMapping(value = "/api/management/products")
    public List<ProductEntity> getProductList(){
        return productOperationFacade.getProducts();
    }

    @GetMapping(value = "/api/management/feedbackEmployees")
    public List<RaportEntity> getEmployeesFeedback(@RequestBody(required = false) LocalDateTime localDateTime) {
        if (localDateTime == null){
            return feedbackService.getEmployeesFeedback(LocalDateTime.now());
        } else {
            return feedbackService.getEmployeesFeedback(localDateTime);
        }
    }

    @GetMapping(value = "/api/management/requestedItems")
    public List<String> getRequestedItems(){
        return productOperationFacade.getRequestedItems();
    }
}
