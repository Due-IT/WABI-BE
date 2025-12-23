package com.wap.wabi.event.repository;

import com.wap.wabi.event.entity.EventStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStatisticRepository extends JpaRepository<EventStatistic, Long> {
}
