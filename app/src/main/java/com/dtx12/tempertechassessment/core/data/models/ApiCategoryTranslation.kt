package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiCategoryTranslation(
    @SerializedName("en_GB")
    val enGB: String?,

    @SerializedName("nl_NL")
    val nlNL: String?
)