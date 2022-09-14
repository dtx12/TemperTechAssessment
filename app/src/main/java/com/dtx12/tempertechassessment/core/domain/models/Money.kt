package com.dtx12.tempertechassessment.core.domain.models

import java.math.BigDecimal

data class Money(
    val currency: String,
    val amount: BigDecimal
)