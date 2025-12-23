package com.wap.wabi.event.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class EventStatistic(
    @Id val eventId: Long,
    var attendanceCount: Long = 0
) {

}