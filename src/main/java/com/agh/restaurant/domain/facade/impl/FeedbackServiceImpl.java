package com.agh.restaurant.domain.facade.impl;

import com.agh.restaurant.domain.dao.FeedbackRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.facade.FeedbackService;
import com.agh.restaurant.domain.model.RaportEntity;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<RaportEntity> getEmployeesFeedback(LocalDateTime now) {
        return Lists.newArrayList(feedbackRepository.findAll()).stream().map(RaportEntity::new)
                .collect(Collectors.toList());

    }

}
