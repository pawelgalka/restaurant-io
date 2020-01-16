package com.agh.restaurant.service;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.ProductItemBuilder;
import com.agh.restaurant.domain.ProductStatus;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.dao.FoodRepository;
import com.agh.restaurant.domain.dao.ProductRepository;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.service.impl.ProductOperationFacadeImpl;
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
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProductOperationFacadeTest {

    private static final String PRODUCT_ITEM = "TEST";
    private static final String PRODUCT_ITEM1 = "TEST1";

    @MockBean
    ProductRepository productRepository;

    @MockBean
    FoodRepository foodRepository;

    @InjectMocks
    @Spy
    ProductOperationFacadeImpl productOperationFacade;

    @BeforeEach
    void init() {
        when(foodRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(productRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    void shouldReturnEmptyProductList() {
        //when
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<String> arrayList = productOperationFacade.getRequestedItems();

        //then
        assertThat(arrayList).isEmpty();
    }

    @Test
    void shouldReturnEmptyProducts() {
        //when
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<ProductEntity> arrayList = productOperationFacade.getProducts();

        //then
        assertThat(arrayList).isEmpty();
    }

    @Test
    void shouldAddProductItemWithNotAvailableStatus() {
        //given
        ProductItem productItem = new ProductItemBuilder().setAmount(0).setName(PRODUCT_ITEM).createProductItem();

        //when
        ProductEntity productEntity = productOperationFacade.addProductItem(productItem);

        //then
        verify(productRepository, times(1)).save(productEntity);
        assertThat(productEntity.getAmount()).isEqualTo(0);
        assertThat(productEntity.getProductStatus()).isEqualTo(ProductStatus.NOT_AVAILABLE);
    }

    @Test
    void shouldAddProductItemWithLowStatus() {
        //given
        ProductItem productItem = new ProductItemBuilder().setAmount(5).setName(PRODUCT_ITEM).createProductItem();

        //when
        ProductEntity productEntity = productOperationFacade.addProductItem(productItem);

        //then
        verify(productRepository, times(1)).save(productEntity);
        assertThat(productEntity.getAmount()).isEqualTo(5);
        assertThat(productEntity.getProductStatus()).isEqualTo(ProductStatus.LOW);
    }

    @Test
    void shouldAddProductItemWithAvailableStatus() {
        //given
        ProductItem productItem = new ProductItemBuilder().setAmount(50).setName(PRODUCT_ITEM).createProductItem();

        //when
        ProductEntity productEntity = productOperationFacade.addProductItem(productItem);

        //then
        verify(productRepository, times(1)).save(productEntity);
        assertThat(productEntity.getAmount()).isEqualTo(50);
        assertThat(productEntity.getProductStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    void shouldAddMenuItemAllItemsPresentAndAvailableCorrectly() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(50);
        productEntity.setProductStatus(ProductStatus.AVAILABLE);

        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setName(PRODUCT_ITEM);
        restaurantMenuItem.setItemsNeededNames(new ArrayList<>(singletonList(PRODUCT_ITEM)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);

        //when
        FoodEntity foodEntity = productOperationFacade.addMenuItem(restaurantMenuItem);

        //then
        assertThat(foodEntity.getAvailable()).isEqualTo(Boolean.TRUE);
        assertThat(foodEntity.getNeededProducts().size()).isEqualTo(1);
    }

    @Test
    void shouldAddMenuItemAllItemsPresentAndNotAvailableCorrectly() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(0);
        productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);

        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setName(PRODUCT_ITEM);
        restaurantMenuItem.setItemsNeededNames(new ArrayList<>(singletonList(PRODUCT_ITEM)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);

        //when
        FoodEntity foodEntity = productOperationFacade.addMenuItem(restaurantMenuItem);

        //then
        assertThat(foodEntity.getAvailable()).isEqualTo(Boolean.FALSE);
        assertThat(foodEntity.getNeededProducts().size()).isEqualTo(1);
    }

    @Test
    void shouldAddMenuItemOneItemMissingFail() {
        //given
        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setName(PRODUCT_ITEM);
        restaurantMenuItem.setItemsNeededNames(new ArrayList<>(singletonList(PRODUCT_ITEM)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                null);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productOperationFacade.addMenuItem(restaurantMenuItem));

        //then
        assertThat(exception.getMessage()).isEqualTo("Some products are not defined in storage.");
    }

    @Test
    void shouldListMenuCorrectly() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(20);
        productEntity.setProductStatus(ProductStatus.AVAILABLE);

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setName(PRODUCT_ITEM);
        foodEntity.setAvailable(true);
        foodEntity.setNeededProducts(new ArrayList<>(singletonList(productEntity)));

        productEntity.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));

        when(foodRepository.findAll()).thenReturn(new ArrayList<>(singletonList(foodEntity)));

        //when
        List<FoodEntity> list = productOperationFacade.getMenuList();

        //then
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo(PRODUCT_ITEM);
    }

    @Test
    void shouldChangeStateOfProductOnAlterAndAvailabilityOfFood() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(0);
        productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setAvailable(false);
        foodEntity.setNeededProducts(new ArrayList<>(singletonList(productEntity)));

        productEntity.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));

        when(foodRepository.findAll()).thenReturn(new ArrayList<>(singletonList(foodEntity)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);
        when(productRepository.findAll()).thenReturn(new ArrayList<>(singletonList(productEntity)));

        ProductItem productItem = new ProductItemBuilder().setAmount(50).setName(PRODUCT_ITEM).createProductItem();

        //when
        List<ProductEntity> productEntities = productOperationFacade
                .alterProductAmount(new ArrayList<>(singletonList(productItem)));

        //then
        assertThat(productEntities.get(0).getProductStatus()).isEqualTo(ProductStatus.AVAILABLE);
        assertThat(productEntities.get(0).getAmount()).isEqualTo(50);
        assertThat(productEntities.get(0).getUsedInFoods().get(0).getAvailable()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shouldChangeStateOfProductOnAlterToNotAvailable() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(0);
        productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setAvailable(false);
        foodEntity.setNeededProducts(new ArrayList<>(singletonList(productEntity)));

        productEntity.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));

        when(foodRepository.findAll()).thenReturn(new ArrayList<>(singletonList(foodEntity)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);
        when(productRepository.findAll()).thenReturn(new ArrayList<>(singletonList(productEntity)));

        ProductItem productItem = new ProductItemBuilder().setAmount(0).setName(PRODUCT_ITEM).createProductItem();

        //when
        List<ProductEntity> productEntities = productOperationFacade
                .alterProductAmount(new ArrayList<>(singletonList(productItem)));

        //then
        assertThat(productEntities.get(0).getProductStatus()).isEqualTo(ProductStatus.NOT_AVAILABLE);
        assertThat(productEntities.get(0).getAmount()).isEqualTo(0);
    }

    @Test
    void shouldChangeStateOfProductOnAlterToLow() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(0);
        productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);
        when(productRepository.findAll()).thenReturn(new ArrayList<>(singletonList(productEntity)));

        ProductItem productItem = new ProductItemBuilder().setAmount(5).setName(PRODUCT_ITEM).createProductItem();

        //when
        List<ProductEntity> productEntities = productOperationFacade
                .alterProductAmount(new ArrayList<>(singletonList(productItem)));

        //then
        assertThat(productEntities.get(0).getProductStatus()).isEqualTo(ProductStatus.LOW);
        assertThat(productEntities.get(0).getAmount()).isEqualTo(5);
    }

    @Test
    void shouldChangeStateOfProductOnAlterAndNotChangeAvailabilityOfFood() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(20);
        productEntity.setProductStatus(ProductStatus.AVAILABLE);

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setAvailable(true);
        foodEntity.setNeededProducts(new ArrayList<>(singletonList(productEntity)));

        productEntity.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));

        when(foodRepository.findAll()).thenReturn(new ArrayList<>(singletonList(foodEntity)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);
        when(productRepository.findAll()).thenReturn(new ArrayList<>(singletonList(productEntity)));

        ProductItem productItem = new ProductItemBuilder().setAmount(50).setName(PRODUCT_ITEM).createProductItem();

        //when
        List<ProductEntity> productEntities = productOperationFacade
                .alterProductAmount(new ArrayList<>(singletonList(productItem)));

        //then
        assertThat(productEntities.get(0).getProductStatus()).isEqualTo(ProductStatus.AVAILABLE);
        assertThat(productEntities.get(0).getAmount()).isEqualTo(70);
        assertThat(productEntities.get(0).getUsedInFoods().get(0).getAvailable()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shouldChangeStateOfProductOnAlterAndNotChangeAvailabilityOfFoodNotAllAvailable() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(20);
        productEntity.setProductStatus(ProductStatus.AVAILABLE);

        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setName(PRODUCT_ITEM1);
        productEntity1.setAmount(0);
        productEntity1.setProductStatus(ProductStatus.NOT_AVAILABLE);

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setAvailable(false);
        foodEntity.setNeededProducts(new ArrayList<>(asList(productEntity, productEntity1)));

        productEntity.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));
        productEntity1.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));

        when(foodRepository.findAll()).thenReturn(new ArrayList<>(singletonList(foodEntity)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);
        when(productRepository.findByName(PRODUCT_ITEM1)).thenReturn(
                productEntity1);
        when(productRepository.findAll()).thenReturn(new ArrayList<>(asList(productEntity, productEntity1)));

        ProductItem productItem = new ProductItemBuilder().setAmount(50).setName(PRODUCT_ITEM).createProductItem();

        //when
        List<ProductEntity> productEntities = productOperationFacade
                .alterProductAmount(new ArrayList<>(singletonList(productItem)));

        //then
        assertThat(productEntities.get(0).getProductStatus()).isEqualTo(ProductStatus.AVAILABLE);
        assertThat(productEntities.get(0).getAmount()).isEqualTo(70);
        assertThat(productEntities.get(0).getUsedInFoods().get(0).getAvailable()).isEqualTo(Boolean.FALSE);
    }

    @Test
    void shouldNotChangeStateOfProductOnAlterNegativeAmount() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(0);
        productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setAvailable(false);
        foodEntity.setNeededProducts(new ArrayList<>(singletonList(productEntity)));

        productEntity.setUsedInFoods(new ArrayList<>(singletonList(foodEntity)));

        when(foodRepository.findAll()).thenReturn(new ArrayList<>(singletonList(foodEntity)));

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(
                productEntity);
        when(productRepository.findAll()).thenReturn(new ArrayList<>(singletonList(productEntity)));

        ProductItem productItem = new ProductItemBuilder().setAmount(-50).setName(PRODUCT_ITEM).createProductItem();

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productOperationFacade
                        .alterProductAmount(new ArrayList<>(singletonList(productItem))));

        //then
        assertThat(exception.getMessage()).isEqualTo("Cannot subtract from storage");

    }

    @Test
    void shouldAddToExistingProductAmount() {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_ITEM);
        productEntity.setAmount(20);
        productEntity.setProductStatus(ProductStatus.AVAILABLE);

        ProductItem productItem = new ProductItemBuilder().setAmount(5).setName(PRODUCT_ITEM).createProductItem();

        when(productRepository.findByName(PRODUCT_ITEM)).thenReturn(productEntity);

        //when
        ProductEntity productEntity1 = productOperationFacade.addProductItem(productItem);

        //then
        assertThat(productEntity1.getAmount()).isEqualTo(25);
    }

    @Test
    void shouldDeleteCorrectly() {
        //given

        //when
        productOperationFacade.deleteMenuItem(1L);

        assertThat(1).isEqualTo(1); //dummy test to validate not throwing exception

        //then
    }
}