package com.wap.wabi.event.event

import com.wap.wabi.event.fixture.EventFixture
import com.wap.wabi.event.fixture.EventStudentFixture
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.event.service.EventAttendaneService
import com.wap.wabi.student.fixture.StudentFixture
import com.wap.wabi.student.repository.StudentRepository
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher
import java.util.*

class AttendanceServiceTest {
    // 의존성 Mocking
    private val eventStudentRepository: EventStudentRepository = mock(EventStudentRepository::class.java)
    private val studentRepository: StudentRepository = mock(StudentRepository::class.java)
    private val eventRepository: EventRepository = mock(EventRepository::class.java)
    private val eventPublisher: ApplicationEventPublisher = mock(ApplicationEventPublisher::class.java)

    private val attendanceService = EventAttendaneService(
        eventRepository, studentRepository, eventStudentRepository, eventPublisher
    )

    @Test
    fun `출석 체크 성공 시 이벤트를 발행한다`() {
        // Given
        val event = EventFixture.createEvent(name = "event", id = 1)
        val student = StudentFixture.createStudent(id = "student1", name = "student")
        val eventStudent = EventStudentFixture.createEventStudent(event = event, student = student)

        val request = CheckInRequest(studentId = "student1", eventId = 1L)

        `when`(studentRepository.findById(any())).thenReturn(Optional.of(eventStudent.student))
        `when`(eventRepository.findById(any())).thenReturn(Optional.of(eventStudent.event))
        `when`(eventStudentRepository.findByStudentAndEvent(any(), any())).thenReturn(Optional.of(eventStudent))

        // When
        attendanceService.checkIn(request)

        // Then
        // 실제로 publishEvent가 호출되었는지 검증
        verify(eventPublisher, times(1)).publishEvent(any(AttendanceCompletedEvent::class.java))
    }
}