package com.agh.restaurant.spring.conditionals;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Conditional(value = FirebaseConditionImpl.class)
public @interface FirebaseCondition {

}
