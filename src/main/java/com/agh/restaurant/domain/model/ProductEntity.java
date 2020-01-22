/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "product")
@Table(name = "PRODUCT")
public class ProductEntity extends AbstractEntity implements Serializable {

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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductEntity))
            return false;
        if (!super.equals(o))
            return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                getProductStatus() == that.getProductStatus() &&
                Objects.equals(getUsedInFoods(), that.getUsedInFoods());
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getAmount(), getProductStatus(), getUsedInFoods());
    }
}
