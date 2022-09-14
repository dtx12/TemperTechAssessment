package com.dtx12.tempertechassessment.core.extensions

import com.dtx12.tempertechassessment.core.domain.models.Money
import java.math.BigDecimal
import java.text.DecimalFormat

fun Money.formatToPrice(): String {
    val decimalFormat = DecimalFormat("0.00")
    return "$currency ${decimalFormat.format(amount)}"
}