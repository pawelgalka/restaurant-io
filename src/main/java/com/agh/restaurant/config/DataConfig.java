package com.agh.restaurant.config;

import com.agh.restaurant.domain.dao.DaoPackage;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = DaoPackage.class)
public class DataConfig {

}
