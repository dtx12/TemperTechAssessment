package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiGeo(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)