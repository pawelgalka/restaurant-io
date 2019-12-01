package com.agh.restaurant.service;

import com.agh.restaurant.domain.*;
import com.agh.restaurant.domain.dao.*;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.service.impl.OrderOperationFacadeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest
class OrderOperationFacadeTest {
    @MockBean
    FoodRepository foodRepository;

    @MockBean
    TableRepository tableRepository;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    FeedbackRepository feedbackRepository;

    @MockBean
    ProductRepository productRepository;

    @InjectMocks
    @Spy
    OrderOperationFacadeImpl orderOperationFacade;

    @BeforeEach
    void init() {
        when(orderRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    void checkSuccessfulFinalizationOfOrder() {
        //given
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId)));

        //when
        OrderEntity orderEntity = orderOperationFacade.finalizeOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.FINALIZED);
    }

    @Test
    void checkIfCreateFeedbackFinalizesOfOrder() {
        //given
        Long orderId = 1L;
        FeedbackPojo feedbackPojo = new FeedbackPojo(FeedbackEnum.EXCELLENT, FeedbackEnum.BAD, FeedbackEnum.BAD);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId)));

        //when
        orderOperationFacade.createFeedback(feedbackPojo, orderId);

        //then
        verify(orderOperationFacade).finalizeOrder(orderId);
    }

    @Test
    void checkIfEmptyOrderReturnsZeroPrice() {
        //given
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId)));

        //when
        Double resultPrice = orderOperationFacade.getOrderPrice(orderId);

        //then
        assertThat(resultPrice).isEqualTo(0);
    }

    @Test
    void checkIfOnlyBeveragesOrderReturnsCorrectPrice() {
        //given
        Long orderId = 1L;

        FoodEntity stubFood = new FoodEntity();
        stubFood.setPrice(1.);
        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withBeverages(new ArrayList<>(
                        asList(stubFood, stubFood, stubFood)))));

        //when
        Double resultPrice = orderOperationFacade.getOrderPrice(orderId);

        //then
        assertThat(resultPrice).isEqualTo(3.);
    }

    @Test
    void checkIfOnlyDishesOrderReturnsCorrectPrice() {
        //given
        Long orderId = 1L;

        FoodEntity stubFood = new FoodEntity();
        stubFood.setPrice(1.);
        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withDishes(new ArrayList<>(
                        asList(stubFood, stubFood, stubFood)))));

        //when
        Double resultPrice = orderOperationFacade.getOrderPrice(orderId);

        //then
        assertThat(resultPrice).isEqualTo(3.);
    }

    @Test
    void checkIfBeveragesAndDishesOrderReturnsCorrectPrice() {
        //given
        Long orderId = 1L;

        FoodEntity stubFood = new FoodEntity();
        stubFood.setPrice(1.);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withDishes(new ArrayList<>(
                        asList(stubFood, stubFood, stubFood))).withBeverages(new ArrayList<>(
                        asList(stubFood, stubFood)))));

        //when
        Double resultPrice = orderOperationFacade.getOrderPrice(orderId);

        //then
        assertThat(resultPrice).isEqualTo(5.);
    }

    @Test
    void checkIfCompletionOfDishAltersProductsWithHighAmountCorrectly(){
        //given
        Long orderId = 1L;

        ProductEntity stubProduct = new ProductEntity();
        stubProduct.setAmount(13);

        FoodEntity stubFood = new FoodEntity();
        stubFood.setNeededProducts(new ArrayList<>(asList(stubProduct)));
        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(stubProduct));

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withDishes(new ArrayList<>(
                        asList(stubFood)))));

        //when
        OrderEntity orderEntity = orderOperationFacade.completeDishOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.DISH_COMPLETE);
        assertThat(orderEntity.getDishes().get(0).getNeededProducts().get(0).getAmount()).isEqualTo(12);
        assertThat(orderEntity.getDishes().get(0).getNeededProducts().get(0).getProductStatus()).isEqualTo(
                ProductStatus.AVAILABLE);

    }

    @Test
    void checkIfCompletionOfDishAltersProductsWithHighOnEdgeAmountCorrectly(){
        //given
        Long orderId = 1L;

        ProductEntity stubProduct = new ProductEntity();
        stubProduct.setAmount(10);

        FoodEntity stubFood = new FoodEntity();
        stubFood.setNeededProducts(new ArrayList<>(asList(stubProduct)));
        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(stubProduct));

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withDishes(new ArrayList<>(
                        asList(stubFood)))));

        //when
        OrderEntity orderEntity = orderOperationFacade.completeDishOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.DISH_COMPLETE);
        assertThat(orderEntity.getDishes().get(0).getNeededProducts().get(0).getAmount()).isEqualTo(9);
        assertThat(orderEntity.getDishes().get(0).getNeededProducts().get(0).getProductStatus()).isEqualTo(
                ProductStatus.LOW);

    }

    @Test
    void checkIfCompletionOfDishAltersProductsWithLowOnEdgeAmountCorrectly(){
        //given
        Long orderId = 1L;

        ProductEntity stubProduct = new ProductEntity();
        stubProduct.setAmount(1);

        FoodEntity stubFood = new FoodEntity();
        stubFood.setNeededProducts(new ArrayList<>(asList(stubProduct)));
        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(stubProduct));

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withDishes(new ArrayList<>(
                        asList(stubFood)))));

        //when
        OrderEntity orderEntity = orderOperationFacade.completeDishOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.DISH_COMPLETE);
        assertThat(orderEntity.getDishes().get(0).getNeededProducts().get(0).getAmount()).isEqualTo(0);
        assertThat(orderEntity.getDishes().get(0).getNeededProducts().get(0).getProductStatus()).isEqualTo(
                ProductStatus.NOT_AVAILABLE);
        assertThat(orderEntity.getDishes().get(0).getAvailable()).isEqualTo(false);

    }

    @Test
    void checkIfCompletionOfBeverageAltersProductsWithHighAmountCorrectly(){
        //given
        Long orderId = 1L;

        ProductEntity stubProduct = new ProductEntity();
        stubProduct.setAmount(13);

        FoodEntity stubFood = new FoodEntity();
        stubFood.setNeededProducts(new ArrayList<>(asList(stubProduct)));
        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(stubProduct));

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withBeverages(new ArrayList<>(
                        asList(stubFood)))));

        //when
        OrderEntity orderEntity = orderOperationFacade.completeBeverageOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.BEVERAGE_COMPLETE);
        assertThat(orderEntity.getBeverages().get(0).getNeededProducts().get(0).getAmount()).isEqualTo(12);
        assertThat(orderEntity.getBeverages().get(0).getNeededProducts().get(0).getProductStatus()).isEqualTo(
                ProductStatus.AVAILABLE);

    }

    @Test
    void checkIfCompletionOfBeverageAltersProductsWithHighOnEdgeAmountCorrectly(){
        //given
        Long orderId = 1L;
        ProductEntity stubProduct = new ProductEntity();
        stubProduct.setAmount(10);

        FoodEntity stubFood = new FoodEntity();
        stubFood.setNeededProducts(new ArrayList<>(asList(stubProduct)));

        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(stubProduct));
        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withBeverages(new ArrayList<>(
                        asList(stubFood)))));

        //when
        OrderEntity orderEntity = orderOperationFacade.completeBeverageOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.BEVERAGE_COMPLETE);
        assertThat(orderEntity.getBeverages().get(0).getNeededProducts().get(0).getAmount()).isEqualTo(9);
        assertThat(orderEntity.getBeverages().get(0).getNeededProducts().get(0).getProductStatus()).isEqualTo(
                ProductStatus.LOW);

    }

    @Test
    void checkIfCompletionOfBeverageAltersProductsWithLowOnEdgeAmountCorrectly(){
        //given
        Long orderId = 1L;

        ProductEntity stubProduct = new ProductEntity();
        stubProduct.setAmount(1);

        FoodEntity stubFood = new FoodEntity();
        stubFood.setNeededProducts(new ArrayList<>(asList(stubProduct)));
        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(stubProduct));

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withId(orderId).withBeverages(new ArrayList<>(
                        asList(stubFood)))));

        //when
        OrderEntity orderEntity = orderOperationFacade.completeBeverageOrder(orderId);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.BEVERAGE_COMPLETE);
        assertThat(orderEntity.getBeverages().get(0).getNeededProducts().get(0).getAmount()).isEqualTo(0);
        assertThat(orderEntity.getBeverages().get(0).getNeededProducts().get(0).getProductStatus()).isEqualTo(
                ProductStatus.NOT_AVAILABLE);
        assertThat(orderEntity.getBeverages().get(0).getAvailable()).isEqualTo(false);

    }

    @Test
    void checkIfCreationOfOrderIsSuccessful(){
        //given
        OrderRequest orderRequest = new OrderRequest();

        orderRequest.setBeverages(new ArrayList<>());
        orderRequest.setDishes(new ArrayList<>());
        orderRequest.setTableId(1L);

        //when
        OrderEntity orderEntity = orderOperationFacade.processOrder(orderRequest);

        //then
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.IN_PROGRESS);
    }
}