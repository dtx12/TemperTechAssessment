package com.dtx12.tempertechassessment.features.shifts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.dtx12.tempertechassessment.core.OneTimeEvent
import com.dtx12.tempertechassessment.core.domain.models.Geo
import com.dtx12.tempertechassessment.core.domain.usecases.CheckLocationPermissionsUseCase
import com.dtx12.tempertechassessment.core.domain.usecases.GetCurrentLocationUseCase
import com.dtx12.tempertechassessment.core.domain.usecases.ListShiftsUseCase
import com.dtx12.tempertechassessment.core.domain.usecases.ListShiftsUseCaseParameters
import com.dtx12.tempertechassessment.core.extensions.distanceTo
import com.dtx12.tempertechassessment.core.extensions.getExceptionFromSource
import com.dtx12.tempertechassessment.core.interactor.UseCase.None
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ShiftsViewModel @Inject constructor(
    private val listShiftsUseCase: ListShiftsUseCase,
    private val checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 50
    }

    private var shiftPagingSource: ShiftsPagingSource? = null
    private var lastLocation: Geo? = null
    private var getLocationJob: Job? = null

    private val _requestPermissionsEvent = MutableLiveData<OneTimeEvent<Unit>>()
    val requestPermissionsEvent: LiveData<OneTimeEvent<Unit>> = _requestPermissionsEvent

    private val _selectedShift = MutableLiveData<ShiftItems.ShiftItem?>()
    val selectedShift: LiveData<ShiftItems.ShiftItem?> = _selectedShift

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Normal)
    val screenState: LiveData<ScreenState> = _screenState

    private val _navigateTo = MutableLiveData<OneTimeEvent<NavigationDestination>>()
    val navigateTo: LiveData<OneTimeEvent<NavigationDestination>> = _navigateTo

    val shiftItemsPagingData: Flow<PagingData<ShiftItems>>

    init {
        val pagerConfig = PagingConfig(PAGE_SIZE, PAGE_SIZE, false, PAGE_SIZE)
        val pager = Pager(pagerConfig, null) {
            val source = ShiftsPagingSource { date ->
                val data = listShiftsUseCase.execute(ListShiftsUseCaseParameters(date))
                getLocationJob?.join()
                data.map { shift ->
                    val distanceToJobInMeters = lastLocation?.let {
                        shift.job.reportAtAddress.geo.distanceTo(it)
                    }
                    ShiftItems.ShiftItem(
                        date = date,
                        shift = shift,
                        distanceToJobInMeters = distanceToJobInMeters
                    )
                }
            }
            shiftPagingSource = source
            source
        }
        shiftItemsPagingData = pager.flow.cachedIn(viewModelScope).map {
            it.insertSeparators { before: ShiftItems.ShiftItem?, after: ShiftItems.ShiftItem? ->
                if (after == null)
                    return@insertSeparators null

                if (before == null) {
                    return@insertSeparators ShiftItems.HeaderItem(after.date)
                }

                if (ChronoUnit.DAYS.between(before.date, after.date) >= 1) {
                    return@insertSeparators ShiftItems.HeaderItem(after.date)
                }

                return@insertSeparators null
            }
        }
        _selectedShift.value = null
        viewModelScope.launch {
            if (checkLocationPermissionsUseCase.execute(None)) {
                tryToGetLocation()
            } else {
                _requestPermissionsEvent.value = OneTimeEvent(Unit)
            }
        }
    }

    private fun tryToGetLocation() {
        getLocationJob = viewModelScope.launch {
            if (lastLocation == null) {
                lastLocation = getLocation()
            }
        }
    }

    private fun invalidateDataSource() {
        shiftPagingSource?.invalidate()
        _screenState.value = ScreenState.Normal
    }

    private suspend fun getLocation(): Geo? {
        return try {
            getCurrentLocationUseCase.execute(None)
        } catch (e: Throwable) {
            null
        }
    }

    fun retry() {
        invalidateDataSource()
    }

    fun reload() {
        tryToGetLocation()
        invalidateDataSource()
    }

    fun selectShift(item: ShiftItems.ShiftItem) {
        _selectedShift.value = item
    }

    fun clearShiftSelection() {
        _selectedShift.value = null
    }

    fun handleAdapterState(state: CombinedLoadStates) {
        val error = state.getExceptionFromSource()
        if (error != null) {
            _screenState.value = ScreenState.Error(error)
        } else {
            _screenState.value = ScreenState.Normal
        }
    }

    fun navigateToSignIn() {
        _navigateTo.value = OneTimeEvent(NavigationDestination.SIGN_IN)
    }

    fun navigateToSignUp() {
        _navigateTo.value = OneTimeEvent(NavigationDestination.SIGN_UP)
    }

    sealed class ScreenState {
        object Normal : ScreenState()
        data class Error(val error: Throwable) : ScreenState()
    }

    enum class NavigationDestination {
        SIGN_IN,
        SIGN_UP
    }
}