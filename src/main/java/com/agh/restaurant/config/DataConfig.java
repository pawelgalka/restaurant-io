package com.agh.restaurant.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.agh.restaurant.domain.dao.DaoPackage;

@EnableJpaRepositories(basePackageClasses = DaoPackage.class)
public class DataConfig {

}
