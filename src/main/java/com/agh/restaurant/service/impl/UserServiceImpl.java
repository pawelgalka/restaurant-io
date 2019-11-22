package com.agh.restaurant.service.impl;

import com.agh.restaurant.config.SecurityConfig.Roles;
import com.agh.restaurant.domain.dao.RoleRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.RoleEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.shared.RegisterUserInit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

    public final static String NAME = "UserService";
    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDao.findByUsername(username);
        if (userDetails == null)
            return null;

        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        for (GrantedAuthority role : userDetails.getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    @Transactional
    @Secured(value = Roles.ROLE_ANONYMOUS)
    public UserEntity registerUser(RegisterUserInit init) {

        UserEntity userLoaded = userDao.findByUsername(init.getUserName());

        if (userLoaded == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(init.getUserName());
            userEntity.setEmail(init.getEmail());

            userEntity.setAuthorities(getUserRoles());
            // TODO firebase users should not be able to login via username and
            // password so for now generation of password is OK
            userEntity.setPassword(UUID.randomUUID().toString());
            userDao.save(userEntity);
            logger.info("registerUser -> user created");
            return userEntity;
        } else {
            logger.info("registerUser -> user exists");
            return userLoaded;
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("USERDAO"+userDao.count());
        if (userDao.count() == 0) {
            //TODO: add some admin based on initial properties of app
        }
    }

    private List<RoleEntity> getAdminRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_ADMIN));
    }

    private List<RoleEntity> getUserRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_USER));
    }

    /**
     * Get or create role
     *
     * @param authority
     * @return
     */
    private RoleEntity getRole(String authority) {
        RoleEntity adminRole = roleRepository.findByAuthority(authority);
        if (adminRole == null) {
            return new RoleEntity(authority);
        } else {
            return adminRole;
        }
    }

}
