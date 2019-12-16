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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderOperationFacadeTest {

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    TableRepository tableRepository;

    @MockBean
    FoodRepository foodRepository;

    @MockBean
    FeedbackRepository feedbackRepository;

    @MockBean
    ReservationRepository reservationRepository;

    @InjectMocks
    @Spy
    OrderOperationFacadeImpl orderOperationFacade;

    @BeforeEach
    void init() {
        when(orderRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    void shouldReturnEmptyList(){
        //given
        when(foodRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<FoodEntity> arrayList = orderOperationFacade.getMenuList();

        //then
        assertThat(arrayList).isEmpty();
    }

    @Test
    void shouldReturnNonNullStatus(){
        //given
        when(orderRepository.findById(any())).thenReturn(
                java.util.Optional.ofNullable(new OrderEntity().withStage(StageEnum.IN_PROGRESS)));

        //when
        StageEnum stageEnum = orderOperationFacade.getOrderStatus(1L);

        //then
        assertThat(stageEnum).isNotNull();
        assertThat(stageEnum).isEqualTo(StageEnum.IN_PROGRESS);
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
        FeedbackPojo feedbackPojo = new FeedbackPojo(FeedbackEnum._5, FeedbackEnum._3, FeedbackEnum._3);

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
        orderRequest.setReservationId(1L);

        //when
//        OrderEntity orderEntity = orderOperationFacade.processOrder(orderRequest);

        //then
        // TODO: 16.12.2019 fix       assertThat(orderEntity.getStage()).isEqualTo(StageEnum.IN_PROGRESS);
    }

    @Test
    void shouldReturnOnlyIncompleteDishes(){
        //given
        OrderEntity orderEntity1 = new OrderEntity().withId(1L).withStage(StageEnum.IN_PROGRESS).withDishes(
                Collections.singletonList(new FoodEntity()));
        OrderEntity orderEntity2 = new OrderEntity().withId(2L).withStage(StageEnum.BEVERAGE_COMPLETE).withDishes(
                Collections.singletonList(new FoodEntity()));
        OrderEntity orderEntity3 = new OrderEntity().withId(3L).withStage(StageEnum.ALL_COMPLETE).withDishes(
                Collections.singletonList(new FoodEntity()));

        List<OrderEntity> orderEntities = Arrays.asList(orderEntity1, orderEntity2, orderEntity3);

        when(orderRepository.findAll()).thenReturn(orderEntities);

        //when
        List<OrderResponse> busyOrders = orderOperationFacade.getIncompleteDishesOrder("test");

        //then
        assertThat(busyOrders.size()).isEqualTo(2);
        assertThat(busyOrders.get(0).getId()).isEqualTo(1L);
        assertThat(busyOrders.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldReturnOnlyIncompleteBeverages(){
        //given
        OrderEntity orderEntity1 = new OrderEntity().withId(1L).withStage(StageEnum.IN_PROGRESS).withBeverages(
                Collections.singletonList(new FoodEntity()));
        OrderEntity orderEntity2 = new OrderEntity().withId(2L).withStage(StageEnum.DISH_COMPLETE).withBeverages(
                Collections.singletonList(new FoodEntity()));;
        OrderEntity orderEntity3 = new OrderEntity().withId(3L).withStage(StageEnum.ALL_COMPLETE).withBeverages(
                Collections.singletonList(new FoodEntity()));;

        List<OrderEntity> orderEntities = Arrays.asList(orderEntity1, orderEntity2, orderEntity3);

        when(orderRepository.findAll()).thenReturn(orderEntities);

        //when
        List<OrderResponse> busyOrders = orderOperationFacade.getIncompleteBeveragesOrder("test");

        //then
        assertThat(busyOrders.size()).isEqualTo(2);
        assertThat(busyOrders.get(0).getId()).isEqualTo(1L);
        assertThat(busyOrders.get(1).getId()).isEqualTo(2L);
    }
}