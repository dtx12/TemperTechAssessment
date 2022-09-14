package com.dtx12.tempertechassessment.core.data.extensions

import com.dtx12.tempertechassessment.core.domain.repositories.ShiftsRepository
import org.threeten.bp.format.DateTimeFormatter

fun ShiftsRepository.QueryParameters.toQueryParametersMap(): Map<String, String> {
    val result = mutableMapOf<String, String>()
    result["filter[date]"] = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    if (onlyFreelance) {
        result["filter[only_freelance]"] = true.toString()
    }
    result["sort"] = sortType.code
    return result
}