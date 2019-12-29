package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

public class RaportEntity extends AbstractEntity{

    @Column(name = "DATE_")
    LocalDateTime date;

    @Lob
    @Column(name = "RAPORT_")
    FeedbackRaport raport;

    public RaportEntity() {
    }

    public RaportEntity(LocalDateTime now, FeedbackRaport feedbackRaport) {
        this.date = now;
        this.raport = feedbackRaport;
    }

    @Override public String toString() {
        return "RaportEntity{" +
                "date=" + date +
                ", raport=" + raport +
                '}';
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FeedbackRaport getRaport() {
        return raport;
    }

    public void setRaport(FeedbackRaport raport) {
        this.raport = raport;
    }
}
