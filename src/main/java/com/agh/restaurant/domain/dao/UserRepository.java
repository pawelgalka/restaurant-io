package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @NonNull
    Optional<UserEntity> findById(@NonNull Long id);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    int deleteByUsername(@NotNull String username);

}
