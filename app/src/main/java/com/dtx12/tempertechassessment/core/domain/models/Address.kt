package com.dtx12.tempertechassessment.core.domain.models


data class Address(
    val zipCode: String,
    val street: String,
    val number: String,
    val numberWithExtra: String,
    val extra: String,
    val city: String,
    val line1: String,
    val line2: String,
    val country: Country,
    val geo: Geo,
    val region: String
)