package com.wap.wabi.statistic.controller

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.statistic.service.StatisticService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/statistic")
class StatisticController(
    private val statisticService: StatisticService
) {
    @GetMapping("/check-in/{eventId}")
    @Operation(
        summary = "특정 이벤트에 체크인한 인원 수를 조회합니다.",
    )
    fun getCheckInStatisticsOfEvent(
        @PathVariable("eventId") eventId: Long,
    ): ResponseEntity<Response> {
        val response = Response.ok(data = statisticService.getAttendanceCountByEvent(eventId))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/check-in/all")
    @Operation(
        summary = "전체 이벤트에 체크인한 인원 수를 조회합니다.",
    )
    fun getCheckInStatisticsOfAll(
        @PathVariable("eventId") eventId: Long,
    ): ResponseEntity<Response> {
        val response = Response.ok(data = statisticService.getAllEventStatistics())
        return ResponseEntity(response, HttpStatus.OK)
    }
}
