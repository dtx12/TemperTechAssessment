package com.dtx12.tempertechassessment.core.domain.models

import org.threeten.bp.ZonedDateTime

data class Shift(
    val id: String,
    val earliestPossibleStartTime: ZonedDateTime,
    val latestPossibleEndTime: ZonedDateTime,
    val averageEstimatedEarningPerHour: Money,
    val job: Job
)