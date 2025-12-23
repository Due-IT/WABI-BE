package com.wap.wabi.statistic.service

import com.wap.wabi.event.entity.EventStatistic
import com.wap.wabi.event.repository.EventStatisticRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StatisticService(
    private val statisticsRepository: EventStatisticRepository
) {
    // 특정 이벤트의 참석자 수 조회
    fun getAttendanceCountByEvent(eventId: Long): Long {
        return statisticsRepository.findById(eventId)
            .map { it.attendanceCount }
            .orElse(0L)
    }

    // 전체 이벤트의 통계 목록 조회 (필요 시)
    fun getAllEventStatistics(): List<EventStatistic> {
        return statisticsRepository.findAll()
    }
}
