package com.dtx12.tempertechassessment.core.domain.usecases

import com.dtx12.tempertechassessment.core.domain.models.Shift
import com.dtx12.tempertechassessment.core.domain.repositories.ShiftsRepository
import com.dtx12.tempertechassessment.core.interactor.UseCase
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ListShiftsUseCase @Inject constructor(
    private val repository: ShiftsRepository
) : UseCase<List<Shift>, ListShiftsUseCaseParameters>() {
    override suspend fun run(params: ListShiftsUseCaseParameters): List<Shift> {
        return repository.listShiftsForDate(
            ShiftsRepository.QueryParameters(
            date = params.date,
            onlyFreelance = true,
            sortType = ShiftsRepository.QueryParameters.SortType.EARLIEST
        ))
    }
}

data class ListShiftsUseCaseParameters(
    val date: LocalDate
)