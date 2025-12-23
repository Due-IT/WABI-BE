package com.wap.wabi.statistic.event

import com.wap.wabi.event.entity.EventStatistic
import com.wap.wabi.event.event.AttendanceCompletedEvent
import com.wap.wabi.event.repository.EventStatisticRepository
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import jakarta.transaction.Transactional
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AttendanceStatisticListener(
    private val statisticsRepository: EventStatisticRepository,
    private val meterRegistry: MeterRegistry
) {

    //    @Async
    @EventListener
    @Transactional
    fun handleAttendanceStatistic(event: AttendanceCompletedEvent) {
        // [1번: 실시간 모니터링 메트릭 적립]. DB가 느려도 실시간으로 확인 가능
        Counter.builder("wabi.attendance.count")
            .tag("eventId", event.eventId.toString()) // 특정 이벤트 ID로 구분
            .description("이벤트별 참석자 수")
            .register(meterRegistry)
            .increment()

        // [2번: DB 통계 업데이트]
        // 나중에 관리자 페이지 등에서 조회할 영구 데이터 저장
        // Timer를 사용해 이 DB 작업이 얼마나 걸리는지도 메트릭으로 측정 가능
        Timer.builder("wabi.statistics.db.update.time")
            .register(meterRegistry)
            .record(Runnable {
                val stats = statisticsRepository.findById(event.eventId)
                    .orElse(EventStatistic(eventId = event.eventId))

                stats.attendanceCount += 1
                statisticsRepository.save(stats)
            })

        println("통계 수집 완료: 이벤트 ID ${event.eventId}")
    }
}