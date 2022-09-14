package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiProject(
    @SerializedName("id")
    val id: String,

    @SerializedName("client")
    val client: ApiClient
)