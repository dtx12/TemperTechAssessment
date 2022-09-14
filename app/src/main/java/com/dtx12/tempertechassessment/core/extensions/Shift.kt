package com.dtx12.tempertechassessment.core.extensions

import com.dtx12.tempertechassessment.core.domain.models.Shift
import org.threeten.bp.format.DateTimeFormatter

fun Shift.formatWorkingHours(): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val start = earliestPossibleStartTime.format(dateTimeFormatter)
    val end = latestPossibleEndTime.format(dateTimeFormatter)
    return "$start - $end"
}