package com.dtx12.tempertechassessment.core.platform

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.dtx12.tempertechassessment.core.domain.models.Geo
import com.dtx12.tempertechassessment.core.extensions.toGeo
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface LocationRequester {
    suspend fun requestLocation(): Geo?
}

@SuppressLint("MissingPermission")
class LocationRequesterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val permissionsChecker: LocationPermissionsChecker
): LocationRequester {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun requestLocation(): Geo? {
        if (!permissionsChecker.hasRequiredPermissions()) {
            return null
        }
        return getCurrentLocation() ?: getLastLocation()
    }

    private suspend fun getCurrentLocation(): Geo? {
        val deferred = CompletableDeferred<Geo?>()
        fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnFailureListener {
                deferred.completeExceptionally(it)
            }.addOnSuccessListener { location: Location? ->
                deferred.complete(location?.toGeo())
            }
        return withTimeoutOrNull(TimeUnit.SECONDS.toMillis(3)) {
            deferred.await()
        }
    }

    private suspend fun getLastLocation(): Geo? {
        val deferred = CompletableDeferred<Geo?>()
        fusedLocationProviderClient
            .lastLocation.addOnFailureListener {
                deferred.completeExceptionally(it)
            }.addOnSuccessListener { location: Location? ->
                deferred.complete(location?.toGeo())
            }
        return deferred.await()
    }
}