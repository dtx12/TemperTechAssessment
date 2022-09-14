package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiCategory(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("name_translation")
    val nameTranslation: ApiCategoryTranslation
)