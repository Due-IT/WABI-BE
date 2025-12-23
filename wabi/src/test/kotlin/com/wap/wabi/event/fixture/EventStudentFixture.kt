package com.wap.wabi.event.fixture

import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.student.entity.Student
import org.springframework.test.util.ReflectionTestUtils

object EventStudentFixture {
    fun createEventStudent(event: Event, student: Student): EventStudent {
        return EventStudent.builder()
            .event(event)
            .student(student)
            .build()
    }

    fun createEventStudent(id: Long, event: Event, student: Student): EventStudent {
        val eventStudent = createEventStudent(event, student)
        ReflectionTestUtils.setField(eventStudent, "id", id)
        return eventStudent
    }
}
