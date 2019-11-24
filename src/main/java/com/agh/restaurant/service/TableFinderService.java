package com.agh.restaurant.service;

import com.agh.restaurant.domain.model.TableEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TableFinderService {
    public List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime);
}
