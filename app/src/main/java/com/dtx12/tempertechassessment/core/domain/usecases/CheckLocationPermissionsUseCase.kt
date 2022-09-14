package com.dtx12.tempertechassessment.core.domain.usecases

import com.dtx12.tempertechassessment.core.interactor.UseCase
import com.dtx12.tempertechassessment.core.platform.LocationPermissionsChecker
import javax.inject.Inject

class CheckLocationPermissionsUseCase @Inject constructor(
    private val locationPermissionsChecker: LocationPermissionsChecker
): UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None): Boolean {
        return locationPermissionsChecker.hasRequiredPermissions()
    }
}