package com.dtx12.tempertechassessment

import androidx.paging.PagingSource
import com.dtx12.tempertechassessment.core.domain.models.*
import com.dtx12.tempertechassessment.features.shifts.ShiftItems
import com.dtx12.tempertechassessment.features.shifts.ShiftsPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.zone.TzdbZoneRulesProvider
import org.threeten.bp.zone.ZoneRulesProvider
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class ShiftsPagingSourceTests {

    @Before
    fun setUp() {
        if (ZoneRulesProvider.getAvailableZoneIds().isEmpty()) {
            val stream = this.javaClass.classLoader!!.getResourceAsStream("TZDB.dat")
            stream.use(::TzdbZoneRulesProvider).apply {
                ZoneRulesProvider.registerProvider(this)
            }
        }
    }

    @Test
    fun `test that paging source loads correct pages`() = runTest {
        val endOfAugust = LocalDate.of(2022, 8, 31)
        val firstSeptember = LocalDate.of(2022, 9, 1)
        val secondSeptember = LocalDate.of(2022, 9, 2)
        val thirdSeptember = LocalDate.of(2022, 9, 3)

        val mockedPage1 = listOf(
            createFakeShiftItem("1", firstSeptember),
            createFakeShiftItem("2", firstSeptember)
        )
        val mockedPage2 = listOf(
            createFakeShiftItem("2", secondSeptember),
            createFakeShiftItem("3", secondSeptember)
        )



        val pageLoader: suspend (LocalDate) -> List<ShiftItems.ShiftItem> = {
            when (it) {
                firstSeptember -> {
                    mockedPage1
                }
                secondSeptember -> {
                    mockedPage2
                }
                else -> throw IllegalStateException()
            }
        }

        val source = ShiftsPagingSource(pageLoader)
        val loadedFirstPage = source.load(PagingSource.LoadParams.Refresh(firstSeptember, 2, false))
        val expectedFirstPage = PagingSource.LoadResult.Page(mockedPage1, endOfAugust, secondSeptember)
        Assert.assertEquals(expectedFirstPage, loadedFirstPage)

        val loadedSecondPage = source.load(PagingSource.LoadParams.Refresh(secondSeptember, 2, false))
        val expectedSecondPage = PagingSource.LoadResult.Page(mockedPage2, firstSeptember, thirdSeptember)
        Assert.assertEquals(loadedSecondPage, expectedSecondPage)
    }

    private fun createFakeShiftItem(id: String, date: LocalDate): ShiftItems.ShiftItem {
        val shift = Shift(
            id = id,
            earliestPossibleStartTime = ZonedDateTime.now(),
            latestPossibleEndTime = ZonedDateTime.now(),
            averageEstimatedEarningPerHour = Money("EUR", BigDecimal(100)),
            job = Job(
                id = id,
                title = "",
                project = Project(
                    id = id,
                    client = Client(
                        id = id,
                        name = "",
                        links = null
                    )
                ),
                category = Category(
                    id = id,
                    name = "",
                    nameTranslation = CategoryTranslation("", "")
                ),
                reportAtAddress = Address(
                    zipCode = "",
                    street = "",
                    number = "",
                    numberWithExtra = "",
                    extra = "",
                    city = "",
                    line1 = "",
                    line2 = "",
                    country = Country(""),
                    geo = Geo(0.0, 0.0),
                    region = ""
                )
            )
        )
        return ShiftItems.ShiftItem(
            date = date,
            shift = shift,
            distanceToJobInMeters = null
        )
    }
}