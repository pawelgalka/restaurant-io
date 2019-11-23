package com.agh.restaurant.domain.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalTime;

@Entity(name = "raport")
@Table(name = "RAPORT")
public class RaportEntity extends AbstractEntity{

    @Column(name = "DATE_")
    LocalTime date;

    @Column(name = "RAPORT_")
    @Convert(converter = RaportConverter.class)
    FeedbackRaport raport;

    public RaportEntity(LocalTime now, FeedbackRaport feedbackRaport) {
        this.date = now;
        this.raport = feedbackRaport;
    }
}
