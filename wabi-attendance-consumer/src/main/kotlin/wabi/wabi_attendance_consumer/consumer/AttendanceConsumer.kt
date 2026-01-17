package wabi.wabi_attendance_consumer.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import wabi.wabi_attendance_consumer.consumer.mail.CheckinMailService
import wabi.wabi_attendance_consumer.dto.AttendanceCompletedEvent

@Component
class AttendanceConsumer(
    private val checkinMailService: CheckinMailService,
    private val statisticsService: StatisticsService
) {
    @KafkaListener(topics = ["checkin"])
    fun process(event: AttendanceCompletedEvent) {
        //1. 메일 전송
        checkinMailService.handleAttendanceEvent(event);
    }

}