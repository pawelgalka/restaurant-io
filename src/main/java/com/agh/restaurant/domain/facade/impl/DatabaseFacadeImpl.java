package com.agh.restaurant.domain.facade.impl;

import com.agh.restaurant.domain.FeedbackEnum;
import com.agh.restaurant.domain.RaportType;
import com.agh.restaurant.domain.dao.FeedbackRepository;
import com.agh.restaurant.domain.dao.RaportRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.facade.DatabaseFacade;
import com.agh.restaurant.domain.model.FeedbackRaport;
import com.agh.restaurant.domain.model.RaportEntity;
import com.agh.restaurant.domain.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "databaseFacade")
public class DatabaseFacadeImpl implements DatabaseFacade {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RaportRepository raportRepository;

    @Override
    public Object getEmployeesFeedback() {
        return null;
    }

    @Override
    public Object getDishesFeedback() {
        return null;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void createEmployeesFeedback() {
        Map<UserEntity, List<FeedbackEnum>> employeesFeedback = new HashMap<>();
        feedbackRepository.findAll().forEach(feedbackEntity -> {
            employeesFeedback.putIfAbsent(userRepository.findById(feedbackEntity.getWaiterId()),
                    new ArrayList<>(Arrays.asList(feedbackEntity.getServiceGrade())));
            employeesFeedback.putIfAbsent(userRepository.findById(feedbackEntity.getBartenderId()),
                    new ArrayList<>(Arrays.asList(feedbackEntity.getBeverageGrade())));
            employeesFeedback.putIfAbsent(userRepository.findById(feedbackEntity.getChefId()),
                    new ArrayList<>(Arrays.asList(feedbackEntity.getDishGrade())));

            employeesFeedback.computeIfPresent(userRepository.findById(feedbackEntity.getWaiterId()),
                    (k,v)->{v.add(feedbackEntity.getServiceGrade()); return v;});
            employeesFeedback.computeIfPresent(userRepository.findById(feedbackEntity.getBartenderId()),
                    (k,v) -> {v.add(feedbackEntity.getBeverageGrade()); return v;});
            employeesFeedback.computeIfPresent(userRepository.findById(feedbackEntity.getChefId()),
                    (k,v) -> {v.add(feedbackEntity.getDishGrade()); return v;});
        });
        Map<UserEntity, Double> res = employeesFeedback.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> entry.getValue().stream().mapToInt(FeedbackEnum::getGrade).average().orElse(0.0)));
        raportRepository.save(new RaportEntity(LocalTime.now(), new FeedbackRaport(RaportType.EMPLOYEE, res)));
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void createDishesFeedback() {

    }


}
