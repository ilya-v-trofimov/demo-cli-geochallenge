package com.nurturecloud.ui

import com.nurturecloud.model.Suburb
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.Scanner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UITest {
    @Test
    fun `readInput should read suburb and postcode`() {
        //given
        val locality = "SyDnEy"
        val postCode = "2000"
        val input = "${locality}\n${postCode}\n"
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)
        val command = Scanner(System.`in`)

        //when
        val (actualLocality, actualPostcode) = readInput(command)

        //then
        assertEquals(locality, actualLocality)
        assertEquals(postCode, actualPostcode)
    }

    @Test
    fun `validateInput should return true if input is valid`() {
        //given
        val locality = "SyDnEy"
        val postCode = "2000"

        //when
        val isInputValid = validateInput(locality, postCode)

        //then
        assertTrue(isInputValid)
    }

    @Test
    fun `validateInput should return false if suburb name is empty`() {
        //given
        val locality = ""
        val postCode = "2000"

        //when
        val isInputValid = validateInput(locality, postCode)

        //then
        assertFalse(isInputValid)
    }

    @Test
    fun `validateInput should return false if postcode is not a four digits number`() {
        //given
        val locality = "SYDNEY"
        val postCode = "200"

        //when
        val isInputValid = validateInput(locality, postCode)

        //then
        assertFalse(isInputValid)
    }

    @Test
    fun `printResults should print results to stdout`() {
        //given
        val sydney = Suburb(2000, "SYDNEY", "NSW", "", "Delivery Area", 151.2099f, -33.8697f)
        val nearbySubs = listOf(
            Suburb(2000, "THE ROCKS", "NSW", "SYDNEY", "Delivery Area", 151.208f, -33.8592f),
            Suburb(2000, "HAYMARKET", "NSW", "", "Delivery Area", 151.2045f, -33.8814f),
            Suburb(2000, "BARANGAROO", "NSW", "", "Delivery Area", 151.2015f, -33.8614f),
        )
        val fringeSubs = listOf(
            Suburb(2113, "MACQUARIE PARK", "NSW", "", "Delivery Area", 151.1242f, -33.7787f),
            Suburb(2153, "BAULKHAM HILLS", "NSW", "", "Delivery Area", 150.9828f, -33.7544f),
        )
        val expectedData = "Nearby Suburbs:\n" +
            "\tTHE ROCKS 2000\n" +
            "\tHAYMARKET 2000\n" +
            "\tBARANGAROO 2000\n" +
            "Fringe Suburbs:\n" +
            "\tMACQUARIE PARK 2113\n" +
            "\tBAULKHAM HILLS 2153\n"
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        //when
        printResults(sydney, nearbySubs, fringeSubs)
        outputStream.flush()
        val writtenData = String(outputStream.toByteArray())

        //then
        assertEquals(expectedData, writtenData)
    }

    @Test
    fun `printError should print error to stdout`() {
        val message = "Test Error Message"
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        val expectedData = "ERROR: ${message}\n"

        //when
        printError(message)
        outputStream.flush()
        val writtenData = String(outputStream.toByteArray())

        //then
        assertEquals(expectedData, writtenData)
    }
}
