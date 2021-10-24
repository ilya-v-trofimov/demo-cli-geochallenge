package com.nurturecloud.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nurturecloud.model.Suburb
import com.nurturecloud.utils.distanceBetweenSuburbs
import java.io.IOException
import java.util.Optional

interface ISuburbRepo {
    fun findSuburb(pcode: Int, locality: String): Optional<Suburb>
    fun findNearbySuburbs(current: Suburb): List<Suburb>
    fun findFringeSuburbs(current: Suburb): List<Suburb>
}

class SuburbRepository(dataFileName: String) : ISuburbRepo {
    companion object {
        fun loadData(resourceFileName: String): List<Suburb> {
            val url = Thread.currentThread().contextClassLoader.getResource(resourceFileName)
            val mapper = jacksonObjectMapper()
            try {
                return mapper.readValue(url, object : TypeReference<List<Suburb>>() {})
            } catch (e: IOException) {
                throw RuntimeException("There was a problem parsing file ${resourceFileName}: " + e.message, e)
            }
        }
    }

    private val suburbs: List<Suburb> = loadData(dataFileName)

    private val maxItems = 15
    private val nearbyRangeKm = 10.0
    private val fringeRangeKm = 50.0

    override fun findSuburb(pcode: Int, locality: String): Optional<Suburb> {
        return Optional.ofNullable(suburbs.find { it.pcode == pcode && it.locality.equals(locality, true) })
    }

    override fun findNearbySuburbs(current: Suburb): List<Suburb> {
        return findSuburbsWithinRange(current, nearbyRangeKm, maxItems)
    }

    override fun findFringeSuburbs(current: Suburb): List<Suburb> {
        val suburbsWitin10kmRange = findSuburbsWithinRange(current, nearbyRangeKm, maxItems)
        val suburbsWitin50kmRange = findSuburbsWithinRange(current, fringeRangeKm, maxItems * 2)
        return suburbsWitin50kmRange.minus(suburbsWitin10kmRange.toSet()).take(maxItems)
    }

    private fun findSuburbsWithinRange(current: Suburb, range: Double, limit: Int): List<Suburb> {
        return suburbs.asSequence().filter { it != current }
            .map { sub -> Pair(sub, distanceBetweenSuburbs(current, sub)) }
            .filter { subWithDistance -> subWithDistance.second?.let { it <= range } ?: false }
            .sortedBy { subWithDistance -> subWithDistance.second }
            .take(limit)
            .map { subWithDistance -> subWithDistance.first }
            .toList()
    }
}
