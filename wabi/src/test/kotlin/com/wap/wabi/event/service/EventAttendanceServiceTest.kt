package com.wap.wabi.event.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.fixture.EventFixture
import com.wap.wabi.event.fixture.EventStudentFixture
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.student.fixture.StudentFixture
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@Transactional
@SpringBootTest
class EventAttendanceServiceTest {

    @Autowired
    private lateinit var eventAttendaneService: EventAttendaneService

    @MockBean
    private lateinit var studentRepository : StudentRepository

    @MockBean
    private lateinit var eventRepository: EventRepository

    @MockBean
    private lateinit var eventStudentRepository: EventStudentRepository


    @Test
    fun 이벤트에_체크인_한다() {
        //Given
        val checkInRequest = CheckInRequest(
            studentId = "201912050", eventId = 1
        )

        val event = EventFixture.createEvent(id = 1, name = "Event 1")
        val band = BandFixture.createBand(id = 1, name = "Band 1")
        val student = StudentFixture.createStudent(id = "201912050", name = "Student1")
        val eventStudent = EventStudentFixture.createEventStudent(id = 1, event = event, student = student)

        Mockito.`when`(studentRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(student))
        Mockito.`when`(eventRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(event))
        Mockito.`when`(eventStudentRepository.findByStudentAndEvent(ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(Optional.of(eventStudent))

        //When
        val result = eventAttendaneService.checkIn(checkInRequest)

        //Then
        assertThat(result).isEqualTo(EventStudentStatus.CHECK_IN)
    }
}