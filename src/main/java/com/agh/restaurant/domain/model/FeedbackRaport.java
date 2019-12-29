package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.RaportType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Map;

public class FeedbackRaport<K,V> implements Serializable {
    public FeedbackRaport(RaportType type, Map<K,V> raport) {
        this.type = type;
        this.raport = raport;
    }

    RaportType type;

    Map<K,V> raport;

    @Override public String toString() {
        return "FeedbackRaport{" +
                "type=" + type +
                ", raport=" + raport +
                '}';
    }

    public RaportType getType() {
        return type;
    }

    public void setType(RaportType type) {
        this.type = type;
    }

    public Map<K, V> getRaport() {
        return raport;
    }

    public void setRaport(Map<K, V> raport) {
        this.raport = raport;
    }
}
