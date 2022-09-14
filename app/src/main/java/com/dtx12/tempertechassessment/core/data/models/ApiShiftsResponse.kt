package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiShiftsResponse(
    @SerializedName("data")
    val data: List<ApiShiftData>
)