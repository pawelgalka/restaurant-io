package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByAuthority(String authority);
}
