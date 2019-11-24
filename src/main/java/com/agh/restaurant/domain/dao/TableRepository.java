package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.TableEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableRepository extends CrudRepository<TableEntity, Long> {

}
