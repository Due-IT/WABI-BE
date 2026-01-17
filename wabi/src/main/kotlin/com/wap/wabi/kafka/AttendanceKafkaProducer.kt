package com.wap.wabi.kafka

import com.wap.wabi.event.event.AttendanceCompletedEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Async
@Component
class AttendanceKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    @EventListener
fun sendAttendanceEvent(event: AttendanceCompletedEvent) {
    println(">>> [Kafka] 전송 시도 시작: ${event.studentId}") // 로그 1

    kafkaTemplate.send("checkin", event.studentId.toString(), event)
        .whenComplete { result, ex ->
            if (ex == null) {
                println(">>> [Kafka] 전송 성공! Offset: ${result.recordMetadata.offset()}") // 로그 2
            } else {
                println(">>> [Kafka] 전송 실패: ${ex.message}") // 에러 로그
            }
        }
    }
}