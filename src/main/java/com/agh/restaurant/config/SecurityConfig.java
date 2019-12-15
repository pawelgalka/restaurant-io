package com.agh.restaurant.config;

import com.agh.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @Configuration
//    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {
//
//        private final UserDetailsService userService;
//
//        @Value("${firebase.enabled}")
//        private Boolean firebaseEnabled;
//
//        private final FirebaseAuthenticationProvider firebaseProvider;
//
//        public AuthenticationSecurity(@Qualifier(value = UserServiceImpl.NAME) UserDetailsService userService,
//                FirebaseAuthenticationProvider firebaseProvider) {
//            this.userService = userService;
//            this.firebaseProvider = firebaseProvider;
//        }
//
//        @Override
//        public void init(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userService);
//            if (firebaseEnabled) {
//                auth.authenticationProvider(firebaseProvider);
//            }
//        }
//    }

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthoritiesSuccessHandler authoritiesSuccessHandler;
    //        @Value("${firebase.enabled}")
    //        private Boolean firebaseEnabled;
    //
    //        public ApplicationSecurity(FirebaseService firebaseService) {
    //            this.firebaseService = firebaseService;
    //        }
    //
    //        @Override
    //        protected void configure(HttpSecurity http) throws Exception {
    //            if (firebaseEnabled) {
    //                http.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter.class).authorizeRequests()//
    //
    //                        .antMatchers("/api/waiter/**").hasRole(Roles.WAITER)//
    //                        .antMatchers("/api/bartender/**").hasRole(Roles.BARTENDER)//
    //                        .antMatchers("/api/cook/**").hasRole(Roles.COOK)//
    //                        .antMatchers("/api/management/**").hasAnyRole(Roles.MANAGER, Roles.ADMIN)//
    //                        .antMatchers("/api/supplier/**").hasRole(Roles.SUPPLIER)//
    //                        .antMatchers("/api/customer/**").permitAll()//
    //                        .antMatchers("/**").denyAll()//
    //                        .and().csrf().disable()//
    //                        .anonymous().authorities(Roles.ROLE_ANONYMOUS);//
    //            }
    //        }
    //
    //        private final FirebaseService firebaseService;
    //
    //        private FirebaseFilter tokenAuthorizationFilter() {
    //            return new FirebaseFilter(firebaseService);
    //        }
    @Autowired
    UserService userService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests().antMatchers("/api/customer/**").permitAll()//
                .antMatchers("/api/waiter/**").hasRole(Roles.WAITER)//
                .antMatchers("/api/bartender/**").hasRole(Roles.BARTENDER)//
                .antMatchers("/api/cook/**").hasRole(Roles.COOK)//
                .antMatchers("/api/management/**").hasRole(Roles.MANAGER)//
                .antMatchers("/api/supplier/**").hasRole(Roles.SUPPLIER)//
                .and().formLogin().loginPage("/login").successHandler(authoritiesSuccessHandler)
                .usernameParameter("username").passwordParameter("password")
                .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setStatus(403);
                }).and().logout().logoutUrl("/logout").permitAll()
                .and().logout().permitAll().and().csrf().disable();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userService);

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

