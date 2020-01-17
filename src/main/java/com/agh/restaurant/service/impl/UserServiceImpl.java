/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.service.impl;

import com.agh.restaurant.config.Roles;
import com.agh.restaurant.domain.dao.RoleRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.RoleEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@DependsOn({ "roleRepository" })
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userDao;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userDao, RoleRepository roleRepository) {
        this.userDao = userDao;
        this.roleRepository = roleRepository;
    }

    public UserDetails loadUserByUsername(String username) {
        logger.error(username);
        UserDetails userDetails = userDao.findByUsername(username);
        if (userDetails == null)
            return null;

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (GrantedAuthority role : userDetails.getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    @Transactional
    public UserEntity registerUser(RegisterUserInit init){
        UserEntity userLoaded = userDao.findByUsername(init.getUsername());
        Objects.requireNonNull(init.getUsername());
        Objects.requireNonNull(init.getPassword());
        if (isNull(userLoaded)) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(init.getUsername());
            newUser.setDisplayName(init.getUsername());
            newUser.setEmail(init.getEmail());
            newUser.setPassword(passwordEncoder.encode(init.getPassword()));
            newUser.setAuthorities(strategyOfRoles.get(init.getRole()));
            userDao.save(newUser);
            return newUser;
        } else {
            return userLoaded;
        }
    }

    @Override public UserEntity updateUser(RegisterUserInit init) {
        UserEntity userLoaded = userDao.findByUsername(init.getUsername());

        if (nonNull(userLoaded)) {
            userLoaded.setUsername(init.getUsername());
            userLoaded.setDisplayName(init.getUsername());
            userLoaded.setEmail(init.getEmail());
            userLoaded.setPassword(passwordEncoder.encode(init.getPassword()));
            userLoaded.setAuthorities(strategyOfRoles.get(init.getRole()));
            userDao.save(userLoaded);
            return userLoaded;
        } else {
            throw new IllegalArgumentException("Cannot update. User does not exist.");
        }
    }

    @Override public List<UserEntity> getUsers() {
        return Lists.newArrayList(userDao.findAll());
    }

    @Override public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

    @Override public void deleteUser(String username) {
        int deleted = userDao.deleteByUsername(username);
        if (deleted == 0){
            throw new EmptyResultDataAccessException(0);
        }
    }

    @PostConstruct
    public void init() {
        strategyOfRoles = Stream.of(new Object[][] {
                { Roles.ROLE_CUSTOMER, getCustomerRoles() },
                { Roles.ROLE_ADMIN, getAdminRoles() },
                { Roles.ROLE_MANAGER, getManagerRoles() },
                { Roles.ROLE_BARTENDER, getBartenderRoles() },
                { Roles.ROLE_COOK, getCookRoles() },
                { Roles.ROLE_SUPPLIER, getSupplierRoles() },
                { Roles.ROLE_WAITER, getWaiterRoles() }
        }).collect(Collectors.toMap(role -> (String) role[0], role -> (List<RoleEntity>) role[1]));

        if (userDao.count() == 0 || roleRepository.findByAuthority(Roles.ROLE_ADMIN) == null) {
            //Fir
            UserEntity funcAccount = new UserEntity();
            funcAccount.setUsername("admin");
            funcAccount.setEmail("func@admin.pl");
            funcAccount.setPassword(passwordEncoder.encode("12345678"));
            List<RoleEntity> roleEntities = new ArrayList<>();
            Stream.of(getAdminRoles(), getWaiterRoles(), getSupplierRoles(), getCookRoles(), getBartenderRoles(),
                    getManagerRoles(), getCustomerRoles()).forEach(roleEntities::addAll);
            funcAccount.setAuthorities(roleEntities);
            userDao.save(funcAccount);

        }
    }

    private List<RoleEntity> getAdminRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_ADMIN));
    }

    private List<RoleEntity> getManagerRoles() {
        return new ArrayList<>(
                Collections.singletonList(getRole(Roles.ROLE_MANAGER)));
    }

    private List<RoleEntity> getWaiterRoles() {
        return new ArrayList<>(
                Collections.singletonList(getRole(Roles.ROLE_WAITER)));
    }

    private List<RoleEntity> getBartenderRoles() {
        return new ArrayList<>(
                Collections.singletonList(getRole(Roles.ROLE_BARTENDER)));
    }

    private List<RoleEntity> getSupplierRoles() {
        return new ArrayList<>(
                Collections.singletonList(getRole(Roles.ROLE_SUPPLIER)));
    }

    private List<RoleEntity> getCookRoles() {
        return new ArrayList<>(Collections.singletonList(getRole(Roles.ROLE_COOK)));
    }

    private List<RoleEntity> getCustomerRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_CUSTOMER));
    }

    private Map<String, List<RoleEntity>> strategyOfRoles;

    private RoleEntity getRole(String authority) {
        RoleEntity adminRole = roleRepository.findByAuthority(authority);
        if (adminRole == null) {
            return new RoleEntity(authority);
        } else {
            return adminRole;
        }
    }

}
