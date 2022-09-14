package com.dtx12.tempertechassessment.core.extensions

import android.location.Location
import com.dtx12.tempertechassessment.core.domain.models.Geo
import com.google.android.gms.maps.model.LatLng

fun Geo.toLatLng(): LatLng {
    return LatLng(lat, lon)
}

fun Geo.distanceTo(other: Geo): Float {
    val thisLocation = Location("A")
    thisLocation.latitude = lat
    thisLocation.longitude = lon

    val otherLocation = Location("B")
    otherLocation.latitude = other.lat
    otherLocation.longitude = other.lon

    return thisLocation.distanceTo(otherLocation)
}