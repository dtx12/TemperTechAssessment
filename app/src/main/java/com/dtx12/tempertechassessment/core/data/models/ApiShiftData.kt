package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

data class ApiShiftData(
    @SerializedName("id")
    val id: String,

    @SerializedName("earliest_possible_start_time")
    val earliestPossibleStartTime: OffsetDateTime,

    @SerializedName("latest_possible_end_time")
    val latestPossibleEndTime: OffsetDateTime,

    @SerializedName("average_estimated_earnings_per_hour")
    val averageEstimatedEarningPerHour: ApiMoney,

    @SerializedName("job")
    val job: ApiJob
)