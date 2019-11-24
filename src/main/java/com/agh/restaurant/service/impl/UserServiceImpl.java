package com.agh.restaurant.service.impl;

import com.agh.restaurant.config.SecurityConfig.Roles;
import com.agh.restaurant.domain.dao.RoleRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.RoleEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Service(value = UserServiceImpl.NAME)
@DependsOn({"roleRepository", "firebaseConfig"})
public class UserServiceImpl implements UserService {

    public final static String NAME = "UserService";
    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private final UserRepository userDao;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userDao, RoleRepository roleRepository) {
        this.userDao = userDao;
        this.roleRepository = roleRepository;
    }

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
    public UserEntity registerUser(RegisterUserInit init) {

        UserEntity userLoaded = userDao.findByEmail(init.getEmail());

        if (isNull(userLoaded)) {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(init.getEmail())
                    .setPassword(init.getPassword())
                    .setDisplayName(init.getDisplayName());

            UserRecord userRecord = null;
            try{
                userRecord = FirebaseAuth.getInstance().createUser(request);
            } catch (FirebaseAuthException ex){
                try{
                    String uid = FirebaseAuth.getInstance().getUserByEmail(init.getEmail()).getUid();
                    FirebaseAuth.getInstance().deleteUser(uid);
                    userRecord = FirebaseAuth.getInstance().createUser(request);
                } catch (FirebaseAuthException ignored){}
            }
            UserEntity newUser = new UserEntity();
            newUser.setUsername(Objects.requireNonNull(userRecord).getUid());
            newUser.setDisplayName(init.getDisplayName());
            newUser.setEmail(init.getEmail());
            newUser.setPassword(init.getPassword());
            newUser.setAuthorities(strategyOfRoles.get(init.getRole()));
            userDao.save(newUser);
            logger.info("func account created");
            return newUser;

        } else {
            logger.info("registerUser -> user exists");
            return userLoaded;
        }
    }

    @PostConstruct
    public void init() {
        strategyOfRoles= Stream.of(new Object[][] {
                {Roles.ROLE_CUSTOMER, getCustomerRoles()},
                {Roles.ROLE_ADMIN, getAdminRoles()},
                {Roles.ROLE_MANAGER, getManagerRoles()},
                {Roles.ROLE_BARTENDER, getBartenderRoles()},
                {Roles.ROLE_COOKER, getCookerRoles()},
                {Roles.ROLE_SUPPLIER, getSupplierRoles()},
                {Roles.ROLE_WAITER, getWaiterRoles()}
        }).collect(Collectors.toMap(role -> (String) role[0], role -> (List<RoleEntity>) role[1]));

        if (userDao.count() == 0 || roleRepository.findByAuthority(Roles.ROLE_ADMIN) == null) {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail("func@admin.pl")
                    .setPassword("12345678")
                    .setDisplayName("func");

            UserRecord userRecord = null;
            try{
                userRecord = FirebaseAuth.getInstance().createUser(request);
            } catch (FirebaseAuthException ex){
                try{
                    String uid = FirebaseAuth.getInstance().getUserByEmail("func@admin.pl").getUid();
                    FirebaseAuth.getInstance().deleteUser(uid);
                    userRecord = FirebaseAuth.getInstance().createUser(request);
                } catch (FirebaseAuthException ignored){}
            }
            UserEntity funcAccount = new UserEntity();
            funcAccount.setUsername(Objects.requireNonNull(userRecord).getUid());
            logger.info(userRecord.getUid());
            funcAccount.setEmail("func@admin.pl");
            funcAccount.setPassword(UUID.randomUUID().toString());
            funcAccount.setAuthorities(strategyOfRoles.get(Roles.ROLE_ADMIN));
            userDao.save(funcAccount);
            logger.info("func account created");

        }
    }

    private List<RoleEntity> getAdminRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_ADMIN));
    }

    private List<RoleEntity> getManagerRoles() {return new ArrayList<>(Arrays.asList(getRole(Roles.ROLE_MANAGER))); }

    private List<RoleEntity> getWaiterRoles() {return new ArrayList<>(Arrays.asList(getRole(Roles.ROLE_WAITER))); }

    private List<RoleEntity> getBartenderRoles() {return new ArrayList<>(Arrays.asList(getRole(Roles.ROLE_BARTENDER))); }

    private List<RoleEntity> getSupplierRoles() {return new ArrayList<>(Arrays.asList(getRole(Roles.ROLE_SUPPLIER))); }

    private List<RoleEntity> getCookerRoles() {return new ArrayList<>(Arrays.asList(getRole(Roles.ROLE_COOKER))); }

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
