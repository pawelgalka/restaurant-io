package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findById(Long id);

    UserEntity findByUsername(String username);

}
