package com.wap.wabi.event.fixture

import com.wap.wabi.common.TestConstants
import com.wap.wabi.event.entity.Event
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime

object EventFixture {
    fun createEvent(name: String, id: Long = 1): Event {
        val event = Event.builder()
            .adminId(TestConstants.ADMIN_ID)
            .name(name)
            .startAt(LocalDateTime.now())
            .endAt(LocalDateTime.now().plusDays(1))
            .eventStudentMaxCount(0)
            .build()
        ReflectionTestUtils.setField(event, "id", id)
        return event
    }
}
