package com.dtx12.tempertechassessment.features.shifts

import com.dtx12.tempertechassessment.core.domain.models.Shift
import org.threeten.bp.LocalDate

sealed class ShiftItems {
    data class HeaderItem(val date: LocalDate) : ShiftItems()
    data class ShiftItem(val date: LocalDate, val shift: Shift, val distanceToJobInMeters: Float?) :
        ShiftItems()
}