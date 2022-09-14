package com.dtx12.tempertechassessment.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiJob(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("project")
    val project: ApiProject,

    @SerializedName("category")
    val category: ApiCategory,

    @SerializedName("report_at_address")
    val reportAtAddress: ApiAddress
)