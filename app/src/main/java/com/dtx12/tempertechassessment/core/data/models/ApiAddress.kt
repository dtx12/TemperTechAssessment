package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiAddress(
    @SerializedName("zip_code")
    val zipCode: String,

    @SerializedName("street")
    val street: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("number_with_extra")
    val numberWithExtra: String,

    @SerializedName("extra")
    val extra: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("line1")
    val line1: String,

    @SerializedName("line2")
    val line2: String,

    @SerializedName("country")
    val country: ApiCountry,

    @SerializedName("geo")
    val geo: ApiGeo,

    @SerializedName("region")
    val region: String
)