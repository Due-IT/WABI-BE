package com.wap.wabi.statistic.event

import com.wap.wabi.event.entity.EventStatistic
import com.wap.wabi.event.event.AttendanceCompletedEvent
import com.wap.wabi.event.repository.EventStatisticRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime
import java.util.*

class AttendanceStatisticListenerTest {

    private lateinit var statisticsRepository: EventStatisticRepository
    private lateinit var meterRegistry: MeterRegistry
    private lateinit var listener: AttendanceStatisticListener

    @BeforeEach
    fun setUp() {
        statisticsRepository = mock(EventStatisticRepository::class.java)
        meterRegistry = SimpleMeterRegistry() // 실제 메트릭이 쌓이는 것을 확인하기 위해 SimpleMeterRegistry(In-memory) 사용
        listener = AttendanceStatisticListener(statisticsRepository, meterRegistry)
    }

    @Test
    @DisplayName("출석 완료 이벤트 발생 시 통계 메트릭을 증가시키고 DB를 업데이트한다")
    fun handleAttendanceStatistic_Success() {
        // Given
        val eventId = 1L
        val event = AttendanceCompletedEvent(
            studentId = "student-123",
            eventId = eventId,
            eventName = "event",
            checkInTime = LocalDateTime.now()
        )

        // 기존 통계 데이터가 있는 상황 가정
        val existingStats = EventStatistic(eventId = eventId)
        ReflectionTestUtils.setField(existingStats, "attendanceCount", 5L) // 기존 5명

        `when`(statisticsRepository.findById(eventId)).thenReturn(Optional.of(existingStats))

        // When
        listener.handleAttendanceStatistic(event)

        // Then: 1. DB 업데이트 검증
        // 5명에서 1명이 추가되어 6명이 저장되어야 함
        verify(statisticsRepository, times(1)).save(argThat {
            it.eventId == eventId && it.attendanceCount == 6L
        })

        // Then: 2. Prometheus 메트릭(Counter) 검증
        val counter = meterRegistry.find("wabi.attendance.count")
            .tag("eventId", eventId.toString())
            .counter()

        assertThat(counter).isNotNull
        assertThat(counter?.count()).isEqualTo(1.0)

        // Then: 3. Timer 메트릭 검증 (DB 업데이트 시간이 기록되었는지)
        val timer = meterRegistry.find("wabi.statistics.db.update.time").timer()
        assertThat(timer).isNotNull
        assertThat(timer?.count()).isEqualTo(1L)
    }

    @Test
    @DisplayName("기존 통계 데이터가 없으면 새로 생성하여 저장한다")
    fun handleAttendanceStatistic_CreateNew() {
        // Given
        val eventId = 99L
        val event = AttendanceCompletedEvent(
            studentId = "new-student",
            eventId = eventId,
            eventName = "event",
            checkInTime = LocalDateTime.now()
        )

        `when`(statisticsRepository.findById(anyLong())).thenReturn(Optional.empty())

        // When
        listener.handleAttendanceStatistic(event)

        // Then
        verify(statisticsRepository).save(argThat {
            it.eventId == eventId && it.attendanceCount == 1L
        })
    }

}