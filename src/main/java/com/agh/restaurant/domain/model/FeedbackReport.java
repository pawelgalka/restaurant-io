package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.RaportType;

import java.util.Map;

public class FeedbackReport<K,V> {
    public FeedbackReport(RaportType type, Map<K,V> report) {
        this.type = type;
        this.report = report;
    }

    private RaportType type;

    private Map<K,V> report;

    @Override public String toString() {
        return "FeedbackRaport{" +
                "type=" + type +
                ", raport=" + report +
                '}';
    }

    public RaportType getType() {
        return type;
    }

    public void setType(RaportType type) {
        this.type = type;
    }

    public Map<K, V> getReport() {
        return report;
    }

    public void setReport(Map<K, V> report) {
        this.report = report;
    }
}
