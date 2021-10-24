package com.nurturecloud.ui

import com.nurturecloud.model.Suburb
import java.util.Scanner

fun readInput(command: Scanner): Pair<String, String> {
    println("Please enter a suburb name: ")
    val suburbName = command.nextLine()

    println("Please enter the postcode: ")
    val postcode = command.nextLine()
    return Pair(suburbName, postcode)
}

fun validateInput(suburbName: String?, postcode: String?): Boolean {
    if (suburbName == null || suburbName.isEmpty()) {
        return false
    }
    if (postcode == null || !("[0-9]{4}".toRegex().matches(postcode))) {
        return false
    }
    return true
}

fun printResults(
    currentSuburb: Suburb,
    nearbySubs: List<Suburb>,
    fringeSubs: List<Suburb>
) {
    if (nearbySubs.isEmpty() && fringeSubs.isEmpty()) {
        println("Nothing found for ${currentSuburb.locality}, ${currentSuburb.pcode}!!\n")
    } else {
        printSuburbs(nearbySubs, "Nearby Suburbs")
        printSuburbs(fringeSubs, "Fringe Suburbs")
    }
}

private fun printSuburbs(results: List<Suburb>, caption: String) {
    if (results.isEmpty()) {
        println("${caption}: not found")
    } else {
        println("${caption}:")
        results.forEach { println("\t${it.locality} ${it.pcode}") }
    }
}

fun printError(msg: String) {
    println("ERROR: $msg")
}
