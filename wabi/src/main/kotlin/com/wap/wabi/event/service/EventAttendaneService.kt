package com.wap.wabi.event.service

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.event.event.AttendanceCompletedEvent
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class EventAttendaneService(
    private val eventRepository: EventRepository,
    private val studentRepository: StudentRepository,
    private val eventStudentRepository: EventStudentRepository,

    private val eventPublisher: ApplicationEventPublisher //스프링 이벤트 발행자
) {
    @Transactional
    fun checkIn(checkInRequest: CheckInRequest): EventStudentStatus {
        val eventStudent = findEventStudent(checkInRequest)

        if (eventStudent.status.equals(EventStudentStatus.CHECK_IN)) {
            throw RestApiException(ErrorCode.ALREADY_CHECK_IN)
        }

        val status = eventStudent.checkIn()

        eventPublisher.publishEvent(
            AttendanceCompletedEvent(
                studentId = eventStudent.student.id,
                eventId = eventStudent.event.id,
                eventName = eventStudent.event.name,
                checkInTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
            )
        )



        return status
    }

    private fun findEventStudent(checkInRequest: CheckInRequest): EventStudent {
        val student = studentRepository.findById(checkInRequest.studentId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_STUDENT) }
        val event = eventRepository.findById(checkInRequest.eventId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }
        val eventStudent = eventStudentRepository.findByStudentAndEvent(student, event)
            .orElseThrow { RestApiException(ErrorCode.UNAUTHORIZED_CHECK_IN) }
        return eventStudent
    }

    @Transactional
    fun patchCheckIn(checkInRequest: CheckInRequest): Boolean {
        val eventStudent = findEventStudent(checkInRequest)

        return eventStudent.patchCheckIn()
    }
}