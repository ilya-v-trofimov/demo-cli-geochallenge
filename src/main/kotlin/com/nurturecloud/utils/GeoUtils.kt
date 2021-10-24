package com.nurturecloud.utils

import com.nurturecloud.model.Suburb

const val EARTH_RADIUS_KM = 6371

fun distanceBetweenSuburbs(suburb1: Suburb, suburb2: Suburb): Double? {
    if (suburb1.latitude == null || suburb1.longitude == null ||
        suburb2.latitude == null || suburb2.longitude == null) {
        return null
    }
    val latDistance: Double = toRad(suburb2.latitude - suburb1.latitude)
    val lonDistance: Double = toRad(suburb2.longitude - suburb1.longitude)
    val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
        Math.cos(toRad(suburb1.latitude)) * Math.cos(toRad(suburb2.latitude)) *
        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return EARTH_RADIUS_KM * c
}

private fun toRad(value: Float): Double {
    return value * Math.PI / 180
}
