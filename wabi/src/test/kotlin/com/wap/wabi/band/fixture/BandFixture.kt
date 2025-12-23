package com.wap.wabi.band.fixture

import com.wap.wabi.band.entity.Band
import com.wap.wabi.common.TestConstants
import org.springframework.test.util.ReflectionTestUtils

object BandFixture {
    fun createBand(name: String): Band {
        return Band.builder()
            .adminId(TestConstants.ADMIN_ID)
            .bandName(name)
            .build()
    }

    fun createBand(name: String, id: Long): Band {
        val band = createBand(name)
        ReflectionTestUtils.setField(band, "id", id)
        return band
    }

    fun createAnotherUserBand(name: String): Band {
        return Band.builder()
            .adminId(TestConstants.ANOTHER_ADMIN_ID)
            .bandName(name)
            .build()
    }

    fun createAnotherUserBand(name: String, id: Long): Band {
        val band = createAnotherUserBand(name)
        ReflectionTestUtils.setField(band, "id", id)
        return band
    }
}
