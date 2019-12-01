package com.agh.restaurant.service;

import com.agh.restaurant.config.TestConfiguration;
import com.agh.restaurant.domain.dao.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class TableOperationFacadeTest {

    @MockBean TableRepository tableRepository1;

    @Autowired
    TableOperationFacade tableOperationFacade;



    public void init(){
//        when(tableRepository.findOne()).
    }

    @Test
    public void cannotCreateReservationWhenNoTablesInDatabase(){
        System.out.println(tableOperationFacade);
    }
}