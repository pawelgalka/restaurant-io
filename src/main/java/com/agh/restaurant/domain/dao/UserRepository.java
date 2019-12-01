package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

}
