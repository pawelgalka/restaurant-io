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
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    public static class Roles {
        static final String ANONYMOUS = "ANONYMOUS";
        static final String USER = "USER";
        static final String ADMIN = "ADMIN";

        private static final String ROLE_ = "ROLE_";
        public static final String ROLE_ANONYMOUS = ROLE_ + ANONYMOUS;
        public static final String ROLE_USER = ROLE_ + USER;
        static public final String ROLE_ADMIN = ROLE_ + ADMIN;
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
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
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

                        .antMatchers("/api/open/**").hasAnyRole(Roles.ANONYMOUS)//
                        .antMatchers("/api/client/**").hasRole(Roles.USER)//
                        .antMatchers("/api/admin/**").hasAnyRole(Roles.ADMIN)//
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
