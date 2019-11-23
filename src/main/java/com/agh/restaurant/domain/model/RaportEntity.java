package com.agh.restaurant.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "raport")
@Table(name = "RAPORT")
public class RaportEntity extends AbstractEntity{

    @Column(name = "DATE_")
    LocalDateTime date;

    @Column(name = "RAPORT_")
    @Convert(converter = RaportConverter.class)
    FeedbackRaport raport;

    public RaportEntity(LocalDateTime now, FeedbackRaport feedbackRaport) {
        this.date = now;
        this.raport = feedbackRaport;
    }
}
