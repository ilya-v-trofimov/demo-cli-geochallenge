package com.nurturecloud.utils

import com.nurturecloud.model.Suburb
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class GeoUtilsKtTest {

    @Test
    fun `distanceBetweenSuburbs should return null if lat or long of one of suburbs is null`() {
        // given
        val suburb1 = Suburb(
            pcode = 9001,
            locality = "test1",
            latitude = 101f,
            longitude = -101f
        )
        val suburb2 = Suburb(
            pcode = 9002,
            locality = "test2",
            longitude = 102f
        )

        // when
        val dist = distanceBetweenSuburbs(suburb1, suburb2)

        // then
        assertNull(dist)
    }

    @Test
    fun `distanceBetweenSuburbs should calculate distance between suburbs`() {
        // given
        val suburb1 = Suburb(
            pcode = 2153,
            locality = "Baulkham Hills",
            latitude = -33.7544f,
            longitude = 150.9828f
        )
        val suburb2 = Suburb(
            pcode = 2000,
            locality = "Sydney",
            latitude = -33.8697f,
            longitude = 151.2099f
        )
        val expectedDistance = 24.59

        // when
        val actualDistance = distanceBetweenSuburbs(suburb1, suburb2)

        // then
        assertNotNull(actualDistance)
        assertEquals(expectedDistance, actualDistance, 0.01)
    }
}
