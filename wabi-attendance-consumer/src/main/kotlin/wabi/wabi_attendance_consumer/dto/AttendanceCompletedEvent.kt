package wabi.wabi_attendance_consumer.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AttendanceCompletedEvent(
    val studentId: String,
    val studentName: String,
    val studentEmail: String,
    val eventId: Long,
    val eventName: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val checkInTime: LocalDateTime
)