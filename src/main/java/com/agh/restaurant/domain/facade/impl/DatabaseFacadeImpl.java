package com.agh.restaurant.domain.facade.impl;

import com.agh.restaurant.domain.dao.FeedbackRepository;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.facade.DatabaseFacade;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.RaportEntity;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "databaseFacade")
public class DatabaseFacadeImpl implements DatabaseFacade {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired OrderRepository orderRepository;

    @Override
    public List<RaportEntity> getEmployeesFeedback(LocalDateTime now) {
        return Lists.newArrayList(feedbackRepository.findAll()).stream().map(feedbackEntity -> {
            OrderEntity orderEntity = orderRepository.findById(feedbackEntity.getId()).orElse(null);
            assert orderEntity != null;
            return new RaportEntity(feedbackEntity, orderEntity.getReservationEntity().getTimeOfReservation()
            );
        })
                .collect(Collectors.toList());

    }

    //    @Override
    //    public Map<UserEntity, Double> createEmployeesFeedback(LocalDateTime dateTime) {
    //        Map<UserEntity, List<FeedbackEnum>> employeesFeedback = new HashMap<>();
    //        Lists.newArrayList(feedbackRepository.findAll()).stream().filter(feedbackEntity ->
    //            dateTime.getMonth().equals(feedbackEntity.getOrderEntity().getReservationEntity().getTimeOfReservation().getMonth()) &&
    //                    dateTime.getYear() == feedbackEntity.getOrderEntity().getReservationEntity().getTimeOfReservation().getYear()
    //        ).forEach(feedbackEntity -> {
    //            employeesFeedback.computeIfPresent(feedbackEntity.getOrderEntity().getWaiter(), (k,v) -> {v.add(feedbackEntity.getServiceGrade()); return v;});
    //            employeesFeedback.putIfAbsent(feedbackEntity.getOrderEntity().getWaiter(), new ArrayList<>(Arrays.asList(feedbackEntity.getServiceGrade())));
    //            employeesFeedback.computeIfPresent(feedbackEntity.getOrderEntity().getBartender(), (k,v) -> {v.add(feedbackEntity.getBeverageGrade()); return v;});
    //            employeesFeedback.putIfAbsent(feedbackEntity.getOrderEntity().getBartender(), new ArrayList<>(Arrays.asList(feedbackEntity.getBeverageGrade())));
    //            employeesFeedback.computeIfPresent(feedbackEntity.getOrderEntity().getChef(), (k,v) -> {v.add(feedbackEntity.getDishGrade()); return v;});
    //            employeesFeedback.putIfAbsent(feedbackEntity.getOrderEntity().getChef(), new ArrayList<>(Arrays.asList(feedbackEntity.getDishGrade())));
    //        });
    //        return employeesFeedback.entrySet().stream().collect(Collectors.toMap(
    //                Map.Entry::getKey,
    //                entry -> entry.getValue().stream().mapToInt(FeedbackEnum::getGrade).average().orElse(0.0)));
    //    }
    //
    //    @Override public Map<FoodEntity, Integer> createDishesFeedback(LocalDateTime dateTime) {
    //        Map<FoodEntity, Integer> dishesFeedback = new HashMap<>();
    //        Lists.newArrayList(feedbackRepository.findAll()).stream().filter(feedbackEntity ->
    //                dateTime.getMonth().equals(feedbackEntity.getOrderEntity().getReservationEntity().getTimeOfReservation().getMonth()) &&
    //                        dateTime.getYear() == feedbackEntity.getOrderEntity().getReservationEntity().getTimeOfReservation().getYear()
    //        ).forEach(feedbackEntity -> feedbackEntity.getOrderEntity().getDishes().forEach( dish ->{
    //            dishesFeedback.computeIfPresent(dish, (k,v) -> {v++; return v;});
    //            dishesFeedback.putIfAbsent(dish, 1);
    //        }));
    //        return dishesFeedback;
    //    }
    //
    //    @Override public Map<FoodEntity, Integer> createBeveragesFeedback(LocalDateTime dateTime) {
    //        Map<FoodEntity, Integer> beveragesFeedback = new HashMap<>();
    //        Lists.newArrayList(feedbackRepository.findAll()).stream().filter(feedbackEntity ->
    //                dateTime.getMonth().equals(feedbackEntity.getOrderEntity().getReservationEntity().getTimeOfReservation().getMonth()) &&
    //                        dateTime.getYear() == feedbackEntity.getOrderEntity().getReservationEntity().getTimeOfReservation().getYear()
    //        ).forEach(feedbackEntity -> feedbackEntity.getOrderEntity().getBeverages().forEach( dish ->{
    //            beveragesFeedback.computeIfPresent(dish, (k,v) -> {v++; return v;});
    //            beveragesFeedback.putIfAbsent(dish, 1);
    //        }));
    //        return beveragesFeedback;
    //    }

}
