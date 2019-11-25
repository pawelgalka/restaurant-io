package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.model.RaportEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiter")
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_WAITER, SecurityConfig.Roles.ROLE_MANAGER})
public class WaiterApi {
    private static Logger logger = Logger.getLogger(WaiterApi.class);

    @Autowired
    private TableOperationFacade tableOperationFacade;

    @Autowired
    private OrderOperationFacade orderOperationFacade;


    @GetMapping(value = "/tables")
    public List<TableEntity> getTables() {
        return tableOperationFacade.getAllTables();
    }

    @PatchMapping(value = "/assign")
    public void assignTableToWaiter(@RequestParam Long tableId,@RequestAttribute("username") String username){
        logger.info(username);

        tableOperationFacade.assignTableToWaiter(tableId,username);
    }

    @PostMapping(value = "/order")
    public void createOrder(@RequestBody OrderRequest orderRequest){
        orderOperationFacade.processOrder(orderRequest);
    }
}
