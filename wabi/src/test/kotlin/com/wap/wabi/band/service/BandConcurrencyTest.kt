package com.wap.wabi.band.service

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.payload.BandStudentDto
import com.wap.wabi.band.payload.request.BandStudentEnrollRequest
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest // 스프링 컨텍스트를 로드함
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BandConcurrencyTest(
    private val bandCommandService: BandCommandService,
    private val bandQueryService: BandQueryService,
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository
) {

    private var testBandId : Long = 1L

    @BeforeEach
    fun setUp() {
        // 밴드 생성 및 저장
        val band = Band.builder()
            .adminId(1L)
            .bandName("band")
            .bandMemo("memo")
            .build()

        val savedBand = bandRepository.save(band)
        testBandId = savedBand.id // 생성된 실제 ID를 보관
    }

    @Test
    @DisplayName("학생 등록과 조회가 동시에 대량으로 발생해도 데이터 정합성이 깨지지 않는다")
    fun concurrencyTest() {
        // given
        val bandId = testBandId
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        // when
        repeat(threadCount) { i ->
            executorService.submit {
                try {
                    if (i % 2 == 0) {
                        // 50개 스레드는 등록 수행
                        val dto = BandStudentDto(
                            studentId = "2025000$i",
                            name = "학생$i",
                            club = "와비",
                            position = "부원",
                            joinDate = LocalDate.now(),
                            college = "공과대학",
                            major = "컴퓨터공학",
                            tel = "010-1234-567$i",
                            academicStatus = "재학"
                        )
                        bandCommandService.enrollBandStudent(bandId, BandStudentEnrollRequest(listOf(dto)))
                    } else {
                        // 50개 스레드는 조회 수행
                        val students = bandQueryService.getBandStudents(bandId)
                        println("현재 조회된 학생 수: ${students.size}")
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await() // 모든 스레드가 끝날 때까지 대기

        // then
        val finalStudents = bandQueryService.getBandStudents(bandId)
        // 등록을 시도한 50명이 모두 안전하게 들어와 있는지 확인
        assertThat(finalStudents.size).isEqualTo(50)
    }

    @AfterEach
    fun tearDown() {
        // 삭제 순서 주의 (자식 테이블부터 삭제)
        bandStudentRepository.deleteAllInBatch()
        bandRepository.deleteAllInBatch()
    }
}
