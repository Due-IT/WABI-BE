package wabi.wabi_attendance_consumer.consumer.mail

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class CheckinMailSender(private val mailSender: JavaMailSender) {
    fun sendAttendanceMail(
        studentId: String,
        studentName: String,
        studentEmail: String,
        eventName: String,
        checkInTime: String
    ) {
        val message = SimpleMailMessage().apply {
            setTo(studentEmail)
            setSubject("[출석 알림] 출석이 정상적으로 완료되었습니다.")
            setText("이벤트: ${eventName}\n학생 ID: ${studentId}\n학생 이름: ${studentName}\n출석 시간: ${checkInTime}\n좋은 시간 되세요!")
        }
        mailSender.send(message)
    }
}