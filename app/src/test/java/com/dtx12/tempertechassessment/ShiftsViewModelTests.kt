package com.dtx12.tempertechassessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.dtx12.tempertechassessment.core.domain.usecases.CheckLocationPermissionsUseCase
import com.dtx12.tempertechassessment.core.domain.usecases.GetCurrentLocationUseCase
import com.dtx12.tempertechassessment.core.domain.usecases.ListShiftsUseCase
import com.dtx12.tempertechassessment.core.exceptions.NoInternetException
import com.dtx12.tempertechassessment.features.shifts.ShiftsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class ShiftsViewModelTests {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var shiftsViewModel: ShiftsViewModel

    @MockK
    private lateinit var listShiftsUseCase: ListShiftsUseCase

    @MockK
    private lateinit var checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase

    @MockK
    private lateinit var getCurrentLocationUseCase: GetCurrentLocationUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test that location permissions asked if they were missing`() = runTest {
        coEvery { listShiftsUseCase.execute(any()) }.returns(listOf())
        coEvery { checkLocationPermissionsUseCase.execute(any()) }.returns(false)
        coEvery { getCurrentLocationUseCase.execute(any()) }.returns(null)
        setupViewModel()
        Assert.assertNotNull(shiftsViewModel.requestPermissionsEvent.value?.value)
    }

    @Test
    fun `test that location permissions do not asked if user have them`() = runTest {
        coEvery { listShiftsUseCase.execute(any()) }.returns(listOf())
        coEvery { checkLocationPermissionsUseCase.execute(any()) }.returns(true)
        coEvery { getCurrentLocationUseCase.execute(any()) }.returns(null)
        setupViewModel()
        Assert.assertNull(shiftsViewModel.requestPermissionsEvent.value?.value)
    }

    @Test
    fun `test that exception during loading data was handled`() = runTest {
        val e = NoInternetException()
        coEvery { listShiftsUseCase.execute(any()) }.throws(e)
        coEvery { checkLocationPermissionsUseCase.execute(any()) }.returns(true)
        coEvery { getCurrentLocationUseCase.execute(any()) }.returns(null)
        setupViewModel()
        shiftsViewModel.handleAdapterState(
            CombinedLoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false),
                source = LoadStates(
                    LoadState.Error(e),
                    LoadState.Error(e),
                    LoadState.Error(e),
                )
            )
        )
        Assert.assertEquals(ShiftsViewModel.ScreenState.Error(e), shiftsViewModel.screenState.value)
    }

    private fun setupViewModel() {
        shiftsViewModel = ShiftsViewModel(
            listShiftsUseCase,
            checkLocationPermissionsUseCase,
            getCurrentLocationUseCase
        )
    }
}