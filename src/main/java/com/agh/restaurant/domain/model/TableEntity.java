package com.agh.restaurant.domain.model;

import javax.persistence.Column;

public class TableEntity extends AbstractEntity {
    @Column(name = "WAITER_ID_")
    Integer waiterId;

    public Integer getWaiterId() {
        return waiterId;
    }
}
