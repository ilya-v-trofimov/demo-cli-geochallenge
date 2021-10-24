package com.nurturecloud.repository

import com.nurturecloud.model.Suburb
import com.nurturecloud.utils.distanceBetweenSuburbs
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SuburbRepositoryTest {

    @Test
    fun `loadData should load test data set`() {
        //given
        val testDataSetFileName = "test_load_and_find.json"
        val expectedSet = listOf(
            Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f),
            Suburb(2000, "SYDNEY SOUTH", "NSW", "", "Test", null, null),
            Suburb(4000, "BRISBANE CITY", "QLD", "test", "Delivery Area", 153.0258f, -27.4704f),
            Suburb(3000, "MELBOURNE", "VIC", null, "Delivery Area", 144.9646f, -37.8112f),
        )

        //when
        val actualSet = SuburbRepository.loadData(testDataSetFileName)

        //then
        assertContentEquals(expectedSet, actualSet)
    }

    @Test
    fun `findSuburb should find a Suburb by locality case insensitive and postcode`() {
        //given
        val suburbRepository = SuburbRepository("test_load_and_find.json")
        val pcode = 2000
        val locality = "sYdNeY"
        val expectedSuburb = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)

        //when
        val suburbOptional = suburbRepository.findSuburb(pcode, locality)

        //then
        assertTrue(suburbOptional.isPresent)
        assertEquals(expectedSuburb, suburbOptional.get())
    }

    @Test
    fun `findSuburb should return optional of null if suburb not found`() {
        //given
        val suburbRepository = SuburbRepository("test_load_and_find.json")
        val pcode = 2001
        val locality = "SYDNEY"

        //when
        val suburbOptional = suburbRepository.findSuburb(pcode, locality)

        //then
        assertFalse(suburbOptional.isPresent)
    }

    @Test
    fun `findNearbySuburbs should find suburbs within 10km range`() {
        //given
        val suburbRepository = SuburbRepository("test_find_nearby_and_fringe.json")
        val suburb = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)
        val expectedNearby = listOf(
            Suburb(2000, "BARANGAROO", "NSW", "", "Delivery Area", 151.2015f, -33.8614f),
            Suburb(2000, "HAYMARKET", "NSW", "", "Delivery Area", 151.2045f, -33.8814f),
            Suburb(2000, "THE ROCKS", "NSW", "SYDNEY", "Delivery Area", 151.208f, -33.8592f),
        )

        //when
        val actualNearby = suburbRepository.findNearbySuburbs(suburb)

        //then
        assertEquals(expectedNearby.size, actualNearby.size)
        assertTrue(expectedNearby.containsAll(actualNearby))
    }

    @Test
    fun `findNearbySuburbs should return suburbs sorted by distance`() {
        //given
        val suburbRepository = SuburbRepository("test_find_nearby_and_fringe.json")
        val sydney = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)

        val barangaroo = Suburb(2000, "BARANGAROO", "NSW", "", "Delivery Area", 151.2015f, -33.8614f)
        val haymarket = Suburb(2000, "HAYMARKET", "NSW", "", "Delivery Area", 151.2045f, -33.8814f)
        val theRocks = Suburb(2000, "THE ROCKS", "NSW", "SYDNEY", "Delivery Area", 151.208f, -33.8592f)
        val expectedNearby = listOf(
            Pair(barangaroo, distanceBetweenSuburbs(barangaroo, sydney)),
            Pair(haymarket, distanceBetweenSuburbs(haymarket, sydney)),
            Pair(theRocks, distanceBetweenSuburbs(theRocks, sydney)),
        ).sortedBy { it.second }.map { it.first }

        //when
        val actualNearby = suburbRepository.findNearbySuburbs(sydney)

        //then
        assertContentEquals(expectedNearby, actualNearby)
    }

    @Test
    fun `findNearbySuburbs should return empty list if nearby suburbs not found`() {
        //given
        val suburbRepository = SuburbRepository("test_find_nearby_and_fringe.json")
        val remoteSuburb = Suburb(3333, "REMOTE SUBURB", "VIC", "", "Delivery Area", 144.9646f, -37.8112f)

        val expectedNearby = emptyList<Suburb>()

        //when
        val actualNearby = suburbRepository.findNearbySuburbs(remoteSuburb)

        //then
        assertContentEquals(expectedNearby, actualNearby)
    }

    @Test
    fun `findNearbySuburbs should limit output to 15 records`() {
        //given
        val suburbRepository = SuburbRepository("test_max_records.json")
        val sydney = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)

        val expectedNearbySuburbsCount = 15

        //when
        val actualFringe = suburbRepository.findNearbySuburbs(sydney)

        //then
        assertEquals(expectedNearbySuburbsCount, actualFringe.size)
    }

    @Test
    fun `findFringeSuburbs should find suburbs within range more 10km and less 50km`() {
        //given
        val suburbRepository = SuburbRepository("test_find_nearby_and_fringe.json")
        val suburb = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)
        val expectedNearby = listOf(
            Suburb(2153, "BAULKHAM HILLS", "NSW", "", "Delivery Area", 150.9828f, -33.7544f),
            Suburb(2113, "MACQUARIE PARK", "NSW", "", "Delivery Area", 151.1242f, -33.7787f),
        )

        //when
        val actualNearby = suburbRepository.findFringeSuburbs(suburb)

        //then
        assertEquals(expectedNearby.size, actualNearby.size)
        assertTrue(expectedNearby.containsAll(actualNearby))
    }

    @Test
    fun `findFringeSuburbs should return suburbs sorted by distance`() {
        //given
        val suburbRepository = SuburbRepository("test_find_nearby_and_fringe.json")
        val sydney = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)

        val macquariePark = Suburb(2113, "MACQUARIE PARK", "NSW", "", "Delivery Area", 151.1242f, -33.7787f)
        val baulkhamHills = Suburb(2153, "BAULKHAM HILLS", "NSW", "", "Delivery Area", 150.9828f, -33.7544f)
        val expectedNearby = listOf(
            Pair(baulkhamHills, distanceBetweenSuburbs(baulkhamHills, sydney)),
            Pair(macquariePark, distanceBetweenSuburbs(macquariePark, sydney)),
        ).sortedBy { it.second }.map { it.first }

        //when
        val actualNearby = suburbRepository.findFringeSuburbs(sydney)

        //then
        assertContentEquals(expectedNearby, actualNearby)
    }

    @Test
    fun `findFringeSuburbs should return empty list if nearby suburbs not found`() {
        //given
        val suburbRepository = SuburbRepository("test_find_nearby_and_fringe.json")
        val remoteSuburb = Suburb(3333, "REMOTE SUBURB", "VIC", "", "Delivery Area", 144.9646f, -37.8112f)

        val expectedFringe = emptyList<Suburb>()

        //when
        val actualFringe = suburbRepository.findFringeSuburbs(remoteSuburb)

        //then
        assertContentEquals(expectedFringe, actualFringe)
    }

    @Test
    fun `findFringeSuburbs should limit output to 15 records`() {
        //given
        val suburbRepository = SuburbRepository("test_max_records.json")
        val sydney = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)

        val expectedFringeSuburbsCount = 15

        //when
        val actualFringe = suburbRepository.findFringeSuburbs(sydney)

        //then
        assertEquals(expectedFringeSuburbsCount, actualFringe.size)
    }
}
