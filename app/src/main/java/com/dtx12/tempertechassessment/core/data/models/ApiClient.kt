package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiClient(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("links")
    val links: ApiClientLinks?
)