package com.wap.wabi.event.event

import java.time.LocalDateTime

data class AttendanceCompletedEvent(
    val studentId: String,
    val eventId: Long,
    val eventName: String,
    val checkInTime: LocalDateTime
)