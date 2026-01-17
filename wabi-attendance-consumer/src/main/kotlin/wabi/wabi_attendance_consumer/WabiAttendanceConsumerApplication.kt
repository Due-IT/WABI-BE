package wabi.wabi_attendance_consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
class WabiAttendanceConsumerApplication

fun main(args: Array<String>) {
	runApplication<WabiAttendanceConsumerApplication>(*args)
}
