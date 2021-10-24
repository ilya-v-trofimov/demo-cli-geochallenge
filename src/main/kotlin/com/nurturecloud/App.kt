package com.nurturecloud

import com.nurturecloud.repository.SuburbRepository
import com.nurturecloud.ui.printError
import com.nurturecloud.ui.printResults
import com.nurturecloud.ui.readInput
import com.nurturecloud.ui.validateInput
import java.util.Scanner

private const val dataFileName = "aus_suburbs.json"

fun main() {
    val command = Scanner(System.`in`)
    val running = true
    val suburbRepository = SuburbRepository(dataFileName)
    while (running) {
        val (suburbName, postcode) = readInput(command)
        val isValidInput = validateInput(suburbName, postcode)
        if (!isValidInput) {
            printError("Entered values are not valid. Please try again")
            continue
        }

        val currentSubOptional = suburbRepository.findSuburb(postcode.toInt(), suburbName)
        if (!currentSubOptional.isPresent) {
            printError("Can't find a suburb based on details provided. Please try again")
            continue
        }
        val currentSub = currentSubOptional.get()
        val nearbySubs = suburbRepository.findNearbySuburbs(currentSub)
        val fringeSubs = suburbRepository.findFringeSuburbs(currentSub)

        printResults(currentSub, nearbySubs, fringeSubs)
    }
    command.close()
}

