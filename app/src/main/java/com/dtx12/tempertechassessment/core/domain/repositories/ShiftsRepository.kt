package com.dtx12.tempertechassessment.core.domain.repositories

import com.dtx12.tempertechassessment.core.domain.models.Shift
import org.threeten.bp.LocalDate


interface ShiftsRepository {

    suspend fun listShiftsForDate(params: QueryParameters): List<Shift>

    data class QueryParameters(
        val date: LocalDate,
        val onlyFreelance: Boolean,
        val sortType: SortType
    ) {
        enum class SortType(val code: String) {
            EARLIEST("earliest")
        }
    }
}