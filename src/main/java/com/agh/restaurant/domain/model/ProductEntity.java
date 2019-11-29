package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity(name = "product")
@Table(name = "PRODUCT")
public class ProductEntity extends AbstractEntity {

    @Column(name = "NAME_")
    private String name;

    @Column(name = "AMOUNT_")
    private Integer amount;

    @Column(name = "PRODUCT_STATUS_")
    @Enumerated(EnumType.STRING)

    private ProductStatus productStatus;

    @ManyToMany(mappedBy = "neededProducts")
    @JsonIgnoreProperties(value = "neededProducts")
    List<FoodEntity> usedInFoods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public List<FoodEntity> getUsedInFoods() {
        return usedInFoods;
    }

    public void setUsedInFoods(List<FoodEntity> usedInFoods) {
        this.usedInFoods = usedInFoods;
    }
}
