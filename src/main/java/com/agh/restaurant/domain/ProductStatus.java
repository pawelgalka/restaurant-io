package com.agh.restaurant.domain;

public enum ProductStatus {
    AVAILABLE("AVAILABLE"),LOW("LOW"),NOT_AVAILABLE("NOT_AVAILABLE");

    private final String available;

    ProductStatus(String available) {
        this.available = available;
    }
}
