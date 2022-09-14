package com.dtx12.tempertechassessment.features.shifts

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

class ShiftsPagingSource(
    private val pageLoader: suspend (LocalDate) -> List<ShiftItems.ShiftItem>
) : PagingSource<LocalDate, ShiftItems.ShiftItem>() {

    private val initialKey = LocalDate.now()

    override fun getRefreshKey(state: PagingState<LocalDate, ShiftItems.ShiftItem>): LocalDate? {
        return null
    }

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, ShiftItems.ShiftItem> {
        return try {
            val key = (params.key ?: initialKey)
            val page = pageLoader.invoke(key)
            val prevKey =
                if (key == initialKey) null else key.minusDays(1)
            val nextKey =
                if (ChronoUnit.MONTHS.between(
                        key,
                        initialKey
                    ) >= 2
                ) null else key.plusDays(1)
            LoadResult.Page(
                data = page,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable = throwable)
        }
    }
}