package com.dtx12.tempertechassessment.features.shifts

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dtx12.tempertechassessment.R
import com.dtx12.tempertechassessment.core.exceptions.NoInternetException
import com.dtx12.tempertechassessment.core.extensions.awaitMap
import com.dtx12.tempertechassessment.core.extensions.toLatLng
import com.dtx12.tempertechassessment.core.extensions.visible
import com.dtx12.tempertechassessment.databinding.FragmentShiftsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShiftsFragment : Fragment() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if ((permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) &&
            (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        ) {
            viewModel.reload()
        }
    }

    private val viewModel: ShiftsViewModel by viewModels()
    private val adapter by lazy { ShiftsAdapter(viewModel) }

    private lateinit var binding: FragmentShiftsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShiftsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeScreenState() {
        viewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                is ShiftsViewModel.ScreenState.Error -> {
                    binding.map.visible(false)
                    binding.shiftsRecyclerView.visible(false)
                    binding.errorLinearLayout.visible(true)
                    val errorResId = when (it.error) {
                        is NoInternetException -> R.string.shifts_no_internet_connection_error
                        else -> R.string.shifts_loading_error
                    }
                    binding.errorTextView.setText(errorResId)
                }
                ShiftsViewModel.ScreenState.Normal -> {
                    binding.map.visible(true)
                    binding.shiftsRecyclerView.visible(true)
                    binding.errorLinearLayout.visible(false)
                    binding.errorTextView.text = null
                }
            }
        }
    }

    private fun observeSelectedShift() {
        val bottomSheetBehavior = getShiftsListBottomSheetBehavior()
        viewModel.selectedShift.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    if (it != null) {
                        bottomSheetBehavior.isDraggable = true
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        binding.buttonsLinearLayout.visible(false)
                        val itemIndex = adapter.snapshot().items.indexOf(it)
                        if (itemIndex != -1) {
                            (binding.shiftsRecyclerView.layoutManager as? LinearLayoutManager)
                                ?.scrollToPositionWithOffset(itemIndex, 0)
                        }
                        
                        val map = binding.map.getFragment<SupportMapFragment>().awaitMap()
                        map.clear()
                        val markerPosition = it.shift.job.reportAtAddress.geo.toLatLng()
                        map.addMarker(
                            MarkerOptions()
                                .title(it.shift.job.title)
                                .position(markerPosition)
                        )?.showInfoWindow()
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                markerPosition,
                                15F
                            )
                        )
                    } else {
                        bottomSheetBehavior.isDraggable = false
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        binding.buttonsLinearLayout.visible(true)
                    }

                }
            }
        }
    }

    private fun observeShiftsBottomSheetEvents() {
        val behavior = getShiftsListBottomSheetBehavior()
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        viewModel.clearShiftSelection()
                    }
                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
    }

    private fun observeLocationPermissionRequestEvents() {
        viewModel.requestPermissionsEvent.observe(viewLifecycleOwner) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun observeShiftItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shiftItemsPagingData.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun handleAdapterEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest {
                    viewModel.handleAdapterState(it)
                }
            }
        }
    }

    private fun observeNavEvents() {
        viewModel.navigateTo.observe(viewLifecycleOwner) {
            val value = it.value ?: return@observe
            when (value) {
                ShiftsViewModel.NavigationDestination.SIGN_IN -> {
                    findNavController().navigate(R.id.shiftsFragmentToSignInFragment)
                }
                ShiftsViewModel.NavigationDestination.SIGN_UP -> {
                    findNavController().navigate(R.id.shiftsFragmentToSignUpFragment)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shiftsRecyclerView.adapter = adapter
        binding.shiftsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.retryButton.setOnClickListener {
            viewModel.retry()
        }
        binding.signInButton.setOnClickListener {
            viewModel.navigateToSignIn()
        }
        binding.signUpButton.setOnClickListener {
            viewModel.navigateToSignUp()
        }

        observeScreenState()
        observeSelectedShift()
        observeShiftsBottomSheetEvents()
        observeLocationPermissionRequestEvents()
        observeShiftItems()
        handleAdapterEvents()
        observeNavEvents()
    }

    private fun getShiftsListBottomSheetBehavior(): BottomSheetBehavior<*> {
        return BottomSheetBehavior.from(binding.shiftsRecyclerView)
    }
}