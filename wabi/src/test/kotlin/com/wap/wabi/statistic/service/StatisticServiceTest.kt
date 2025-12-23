package com.wap.wabi.statistic.service

import com.wap.wabi.event.entity.EventStatistic
import com.wap.wabi.event.repository.EventStatisticRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

@ExtendWith(MockitoExtension::class)
class StatisticServiceTest {

    private lateinit var statisticsRepository: EventStatisticRepository
    private lateinit var statisticService: StatisticService

    @BeforeEach
    fun setUp() {
        statisticsRepository = mock(EventStatisticRepository::class.java)
        statisticService = StatisticService(statisticsRepository)
    }

    @Test
    @DisplayName("특정 이벤트 ID로 조회 시 저장된 참석자 수를 정확히 반환한다")
    fun getAttendanceCountByEvent_Success() {
        // Given
        val eventId = 1L
        val expectCount = 50L
        val statistic = EventStatistic(eventId = eventId)

        // ReflectionTestUtils를 사용하여 ID와 카운트 주입 (필드 수정이 불가능할 경우 대비)
        ReflectionTestUtils.setField(statistic, "attendanceCount", expectCount)

        `when`(statisticsRepository.findById(eventId)).thenReturn(Optional.of(statistic))

        // When
        val result = statisticService.getAttendanceCountByEvent(eventId)

        // Then
        assertThat(result).isEqualTo(expectCount)
        verify(statisticsRepository, times(1)).findById(eventId)
    }

    @Test
    @DisplayName("조회된 결과가 없을 경우 에러 대신 0L을 반환한다")
    fun getAttendanceCountByEvent_ReturnZero() {
        // Given
        val eventId = 999L
        `when`(statisticsRepository.findById(eventId)).thenReturn(Optional.empty())

        // When
        val result = statisticService.getAttendanceCountByEvent(eventId)

        // Then
        assertThat(result).isEqualTo(0L)
    }

    @Test
    @DisplayName("전체 통계 목록을 조회하면 리포지토리의 모든 데이터를 반환한다")
    fun getAllEventStatistics_Success() {
        // Given
        val statsList = listOf(
            EventStatistic(eventId = 1L),
            EventStatistic(eventId = 2L)
        )
        `when`(statisticsRepository.findAll()).thenReturn(statsList)

        // When
        val result = statisticService.getAllEventStatistics()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(statsList)
        verify(statisticsRepository, times(1)).findAll()
    }
}
