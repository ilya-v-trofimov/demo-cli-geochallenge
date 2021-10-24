package com.nurturecloud.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Suburb(
    @JsonProperty("Pcode") val pcode: Int,
    @JsonProperty("Locality") val locality: String,
    @JsonProperty("State") val state: String? = null,
    @JsonProperty("Comments") val comments: String? = null,
    @JsonProperty("Category") val category: String? = null,
    @JsonProperty("Longitude") val longitude: Float? = null,
    @JsonProperty("Latitude") val latitude: Float? = null
)
