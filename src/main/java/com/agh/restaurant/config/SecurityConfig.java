package com.agh.restaurant.config;

import com.agh.restaurant.config.auth.firebase.FirebaseAuthenticationProvider;
import com.agh.restaurant.config.auth.firebase.FirebaseFilter;
import com.agh.restaurant.service.FirebaseService;
import com.agh.restaurant.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    public static class Roles {
        static final String ANONYMOUS = "ANONYMOUS";
        static final String ADMIN = "ADMIN";
        static final String MANAGER = "MANAGER";
        static final String WAITER = "WAITER";
        static final String BARTENDER = "BARTENDER";
        static final String SUPPLIER = "SUPPLIER";
        static final String CUSTOMER = "CUSTOMER";
        static final String COOKER = "COOKER";

        private static final String ROLE_ = "ROLE_";
        public static final String ROLE_ANONYMOUS = ROLE_ + ANONYMOUS;
        public static final String ROLE_ADMIN = ROLE_ + ADMIN;
        public static final String ROLE_MANAGER = ROLE_ + MANAGER;
        public static final String ROLE_WAITER = ROLE_ + WAITER;
        public static final String ROLE_BARTENDER = ROLE_ + BARTENDER;
        public static final String ROLE_SUPPLIER = ROLE_ + SUPPLIER;
        public static final String ROLE_CUSTOMER = ROLE_ + CUSTOMER;
        public static final String ROLE_COOKER = ROLE_ + COOKER;

    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

        private final UserDetailsService userService;

        @Value("${firebase.enabled}")
        private Boolean firebaseEnabled;

        private final FirebaseAuthenticationProvider firebaseProvider;

        public AuthenticationSecurity(@Qualifier(value = UserServiceImpl.NAME) UserDetailsService userService, FirebaseAuthenticationProvider firebaseProvider) {
            this.userService = userService;
            this.firebaseProvider = firebaseProvider;
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService);
            if (firebaseEnabled) {
                auth.authenticationProvider(firebaseProvider);
            }
        }
    }

    @Configuration
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Value("${firebase.enabled}")
        private Boolean firebaseEnabled;

        public ApplicationSecurity(FirebaseService firebaseService) {
            this.firebaseService = firebaseService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if (firebaseEnabled) {
                http.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter.class).authorizeRequests()//

                        .antMatchers("/api/waiter/**").hasRole(Roles.WAITER)//
                        .antMatchers("/api/bartender/**").hasRole(Roles.BARTENDER)//
                        .antMatchers("/api/cooker/**").hasRole(Roles.COOKER)//
                        .antMatchers("/api/management/**").hasAnyRole(Roles.MANAGER, Roles.ADMIN)//
                        .antMatchers("/api/supplier/**").hasRole(Roles.SUPPLIER)//
                        .antMatchers("/api/customer/**").permitAll()//
                        .antMatchers("/**").denyAll()//
                        .and().csrf().disable()//
                        .anonymous().authorities(Roles.ROLE_ANONYMOUS);//
            }
        }

        private final FirebaseService firebaseService;

        private FirebaseFilter tokenAuthorizationFilter() {
            return new FirebaseFilter(firebaseService);
        }

    }
}
