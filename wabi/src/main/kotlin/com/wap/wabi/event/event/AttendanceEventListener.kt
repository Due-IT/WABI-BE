package com.wap.wabi.event.event

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

//@Async
@Component
class AttendanceEventListener(
    private val meterRegistry: MeterRegistry
) {

    private val attendanceCounter = meterRegistry.counter("attendance.total.count")

    @EventListener
    fun handleAttendanceEvent(event: AttendanceCompletedEvent) {
        // 1. 여기서 실제 알림 발송 로직이나 통계 업데이트를 수행합니다.
        println("알림 발송 시작: 학생 ${event.studentId}님, 출석 완료!")

        // 성과 비교를 위해 인위적인 지연(1초)을 추가해봅니다.
        Thread.sleep(1000)

        println("알림 발송 완료: ${event.checkInTime}")
    }
}