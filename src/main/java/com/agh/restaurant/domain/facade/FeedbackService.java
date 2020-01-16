package com.agh.restaurant.domain.facade;

import com.agh.restaurant.domain.model.RaportEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackService {
    abstract List<RaportEntity> getEmployeesFeedback(LocalDateTime now);

}
