package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ApiMoney(
    @SerializedName("currency")
    val currency: String,

    @SerializedName("amount")
    val amount: BigDecimal
)