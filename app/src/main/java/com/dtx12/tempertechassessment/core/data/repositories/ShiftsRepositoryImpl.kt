package com.dtx12.tempertechassessment.core.data.repositories

import com.dtx12.tempertechassessment.core.data.endpoints.TemperApi
import com.dtx12.tempertechassessment.core.data.extensions.toDomain
import com.dtx12.tempertechassessment.core.data.extensions.toQueryParametersMap
import com.dtx12.tempertechassessment.core.domain.models.Shift
import com.dtx12.tempertechassessment.core.domain.repositories.ShiftsRepository
import com.dtx12.tempertechassessment.core.exceptions.NoInternetException
import com.dtx12.tempertechassessment.core.platform.NetworkHandler
import javax.inject.Inject

class ShiftsRepositoryImpl @Inject constructor(
    private val dataSource: TemperApi,
    private val networkHandler: NetworkHandler
) : ShiftsRepository {

    override suspend fun listShiftsForDate(params: ShiftsRepository.QueryParameters): List<Shift> {
        if (networkHandler.isNetworkAvailable()) {
            return dataSource.loadShifts(params.toQueryParametersMap()).data.map { it.toDomain() }
        } else {
            throw NoInternetException()
        }
    }
}