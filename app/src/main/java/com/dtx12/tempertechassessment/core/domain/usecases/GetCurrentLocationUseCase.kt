package com.dtx12.tempertechassessment.core.domain.usecases

import com.dtx12.tempertechassessment.core.domain.models.Geo
import com.dtx12.tempertechassessment.core.interactor.UseCase
import com.dtx12.tempertechassessment.core.platform.LocationRequester
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRequester: LocationRequester
): UseCase<Geo?, UseCase.None>() {

    override suspend fun run(params: None): Geo? {
        return locationRequester.requestLocation()
    }
}