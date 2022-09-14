package com.dtx12.tempertechassessment.core.extensions

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.CompletableDeferred

suspend fun SupportMapFragment.awaitMap(): GoogleMap {
    val deferred = CompletableDeferred<GoogleMap>()
    getMapAsync {
        deferred.complete(it)
    }
    return deferred.await()
}