package com.dtx12.tempertechassessment.core.extensions

import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

fun OffsetDateTime.applySystemZone(): ZonedDateTime {
    return toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault())
}