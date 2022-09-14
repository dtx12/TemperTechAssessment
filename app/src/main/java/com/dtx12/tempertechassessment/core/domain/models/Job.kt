package com.dtx12.tempertechassessment.core.domain.models

import org.threeten.bp.OffsetDateTime

data class Job(
    val id: String,
    val title: String,
    val project: Project,
    val category: Category,
    val reportAtAddress: Address
)