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

import java.time.LocalDateTime;
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
    public RaportEntity getEmployeesFeedback(LocalDateTime now) {
        return raportRepository.getByDate_MonthAndDate_Year(now.getMonth().getValue(), now.getYear());
    }

    @Override
    public RaportEntity getDishesFeedback() {
        return null;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void createEmployeesFeedback() {
        Map<UserEntity, List<FeedbackEnum>> employeesFeedback = new HashMap<>();

        feedbackRepository.findAll().forEach(feedbackEntity -> {
            employeesFeedback.compute(userRepository.findById(feedbackEntity.getWaiter().getId()), (w, prev) -> {
                if (prev != null) {
                    prev.add(feedbackEntity.getServiceGrade());
                    return prev;
                } else {
                    return new ArrayList<>(
                            Collections.singleton(feedbackEntity.getServiceGrade()));
                }
            });
        });
        //            employeesFeedback.merge(userRepository.findById(feedbackEntity.getWaiter().getId()), new ArrayList<>(Arrays.asList(feedbackEntity.getServiceGrade())), (n,c) ->)
        //            employeesFeedback.putIfAbsent(userRepository.findById(feedbackEntity.getWaiter().getId()),
        //                    new ArrayList<>(Arrays.asList(feedbackEntity.getServiceGrade())));
        //            employeesFeedback.putIfAbsent(userRepository.findById(feedbackEntity.getBartender().getId()),
        //                    new ArrayList<>(Arrays.asList(feedbackEntity.getBeverageGrade())));
        //            employeesFeedback.putIfAbsent(userRepository.findById(feedbackEntity.getChef().getId()),
        //                    new ArrayList<>(Arrays.asList(feedbackEntity.getDishGrade())));
        //
        //            employeesFeedback.computeIfPresent(userRepository.findById(feedbackEntity.getWaiter().getId()),
        //                    (k,v)->{v.add(feedbackEntity.getServiceGrade()); return v;});
        //            employeesFeedback.computeIfPresent(userRepository.findById(feedbackEntity.getBartender().getId()),
        //                    (k,v) -> {v.add(feedbackEntity.getBeverageGrade()); return v;});
        //            employeesFeedback.computeIfPresent(userRepository.findById(feedbackEntity.getChef().getId()),
        //                    (k,v) -> {v.add(feedbackEntity.getDishGrade()); return v;});
        //        });
        Map<UserEntity, Double> res = employeesFeedback.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream().mapToInt(FeedbackEnum::getGrade).average().orElse(0.0)));
        raportRepository.save(new RaportEntity(LocalDateTime.now(), new FeedbackRaport(RaportType.EMPLOYEE, res)));
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void createDishesFeedback() {

    }

}
