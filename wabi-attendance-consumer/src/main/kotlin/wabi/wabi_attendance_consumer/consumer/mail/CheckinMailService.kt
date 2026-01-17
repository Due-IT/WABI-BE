package wabi.wabi_attendance_consumer.consumer.mail

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import wabi.wabi_attendance_consumer.dto.AttendanceCompletedEvent

@Async
@Component
class CheckinMailService(
    private val mailSender: MailSender,
    private val meterRegistry: MeterRegistry
) {
    fun handleAttendanceEvent(event: AttendanceCompletedEvent) {
        // 2. 실제 이메일 발송 로직 호출
        try {
            println("이메일 발송 시작: 학생 ${event.studentId}")

            mailSender.sendAttendanceMail(
                event.studentId,
                event.studentName,
                event.studentEmail,
                event.eventName,
                event.checkInTime.toString()
            )

//            recordEmailMetric(event, "success")

            println("이메일 발송 완료")
        } catch (e: Exception) {
            // 이메일 발송 실패 시 로깅 (실제 서비스에서는 중요함)
            println("이메일 발송 실패: ${e.message}")
        }
    }

    private fun recordEmailMetric(event: AttendanceCompletedEvent, status: String) {
        Counter.builder("wabi.attendance.email.sent.count")
            .tag("eventId", event.eventId.toString())
            .tag("status", status) // 성공/실패 여부를 태그로 관리
            .description("이벤트별 이메일 발송 결과 카운트")
            .register(meterRegistry)
            .increment()
    }

}