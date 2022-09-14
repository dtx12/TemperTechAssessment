package com.dtx12.tempertechassessment.features.shifts

import android.content.Context
import com.dtx12.tempertechassessment.R
import java.text.DecimalFormat

object DistanceFormatter {

    fun format(distanceInMeters: Float, context: Context): String {
        val decimalFormat = DecimalFormat("0")
        if (distanceInMeters < 1000) {
            return context.getString(
                R.string.shifts_m_format,
                decimalFormat.format(distanceInMeters)
            )
        }
        val distanceInKilometers = distanceInMeters / 1000
        return context.getString(
            R.string.shifts_km_format,
            decimalFormat.format(distanceInKilometers)
        )
    }
}