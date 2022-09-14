package com.dtx12.tempertechassessment.core.platform

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface LocationPermissionsChecker {
    fun hasRequiredPermissions(): Boolean
}

class LocationPermissionsCheckerImpl @Inject constructor(@ApplicationContext private val context: Context): LocationPermissionsChecker {
    override fun hasRequiredPermissions(): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                 ContextCompat
                    .checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}