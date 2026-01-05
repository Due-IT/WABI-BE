package com.wap.wabi.event.event

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

//@Async
@Component
class AttendanceEventListener(
    private val meterRegistry: MeterRegistry,
    private val mailService: CheckInMailService
) {

    private val attendanceCounter = meterRegistry.counter("attendance.total.count")

    @EventListener
    fun handleAttendanceEvent(event: AttendanceCompletedEvent) {
        // 2. 실제 이메일 발송 로직 호출
        try {
            println("이메일 발송 시작: 학생 ${event.studentId}")

            mailService.sendAttendanceMail(
                event.studentId,
                event.studentName,
                event.studentEmail,
                event.eventName,
                event.checkInTime.toString()
            )

            println("이메일 발송 완료")
        } catch (e: Exception) {
            // 이메일 발송 실패 시 로깅 (실제 서비스에서는 중요함)
            println("이메일 발송 실패: ${e.message}")
        }
    }
}