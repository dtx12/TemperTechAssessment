package com.dtx12.tempertechassessment.core.data.endpoints

import com.dtx12.tempertechassessment.core.data.models.ApiShiftsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TemperApi {

    @GET("shifts")
    suspend fun loadShifts(
        @QueryMap params: Map<String, String>
    ): ApiShiftsResponse
}