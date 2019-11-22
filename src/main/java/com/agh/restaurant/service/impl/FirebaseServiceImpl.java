package com.agh.restaurant.service.impl;

import com.agh.restaurant.config.auth.firebase.FirebaseTokenHolder;
import com.agh.restaurant.service.FirebaseService;
import com.agh.restaurant.service.shared.FirebaseParser;
import com.agh.restaurant.spring.conditionals.FirebaseCondition;
import org.springframework.stereotype.Service;

@Service
@FirebaseCondition
public class FirebaseServiceImpl implements FirebaseService {
    @Override
    public FirebaseTokenHolder parseToken(String firebaseToken) {
        return new FirebaseParser().parseToken(firebaseToken);
    }
}
