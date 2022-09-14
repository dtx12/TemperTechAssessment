package com.dtx12.tempertechassessment.core.extensions

import android.location.Location
import com.dtx12.tempertechassessment.core.domain.models.Geo

fun Location.toGeo(): Geo {
    return Geo(lat = latitude, lon = longitude)
}