package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.RaportType;

import java.io.Serializable;
import java.util.Map;

public class FeedbackRaport implements Serializable {
    public FeedbackRaport(RaportType type, Map<UserEntity, Double> raport) {
        this.type = type;
        this.raport = raport;
    }

    RaportType type;
    Map<UserEntity, Double> raport;



}
