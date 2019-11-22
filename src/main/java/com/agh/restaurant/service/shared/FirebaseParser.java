package com.agh.restaurant.service.shared;

import com.agh.restaurant.config.auth.firebase.FirebaseTokenHolder;
import com.agh.restaurant.service.exception.FirebaseTokenInvalidException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import static org.apache.commons.lang.StringUtils.isBlank;

public class FirebaseParser {
    public FirebaseTokenHolder parseToken(String idToken) {
        if (isBlank(idToken)) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return new FirebaseTokenHolder(decodedToken);
        } catch (Exception e) {
            throw new FirebaseTokenInvalidException(e.getMessage());
        }
    }
}
