package com.sparklead.evocharge.ui.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentChargingListBinding
import com.sparklead.evocharge.models.ChargingStation
import com.sparklead.evocharge.models.ChargingStationResponse
import com.sparklead.evocharge.ui.adapters.StationListAdapter
import com.sparklead.evocharge.ui.base.BaseFragment
import com.sparklead.evocharge.ui.states.ChargingStationUiState
import com.sparklead.evocharge.ui.viewmodels.ChargingListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ChargingListFragment : BaseFragment() {

    private var _binding: FragmentChargingListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: ChargingListViewModel
    private lateinit var adapter: StationListAdapter

    private lateinit var currentTime: String
    private lateinit var timeFormat: DateFormat
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: LatLng

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChargingListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ChargingListViewModel::class.java]
        viewModel.getChargingList()

        timeFormat = SimpleDateFormat("hh:mm aaa", Locale.US)
        currentTime = timeFormat.format(Date())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

            if (location != null) {
                lastLocation = LatLng(location.latitude, location.longitude)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.chargingUiState.collect {
                when (it) {
                    is ChargingStationUiState.Error -> {
                        hideLoading()
                        error(it.message)
                    }

                    is ChargingStationUiState.Loading -> {
                        showLoadingDialog()
                    }

                    is ChargingStationUiState.Success -> {
                        showList(it.list)
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
    }

    private fun error(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    private fun showList(list: ArrayList<ChargingStationResponse>) {

        val chargingList: ArrayList<ChargingStation> = ArrayList()
        for (it in list) {

            val temp = ChargingStation(
                name = it.stationName,
                location = it.location,
                available = checkAvailability(it.openingTime, it.closingTime),
                distance = calculateDistance(it.latitude, it.longitude),
                openingTime = it.openingTime,
                closingTime = it.closingTime,
                chargeType = it.chargingType,
                completeAddress = it.completeAddress,
                images = it.images,
                latitude = it.latitude,
                longitude = it.longitude
            )
            chargingList.add(temp)
        }

        hideLoading()

        adapter = StationListAdapter(chargingList)
        binding.rvChargingStation.adapter = adapter
        binding.rvChargingStation.layoutManager = LinearLayoutManager(requireContext())

        adapter.onItemClick = {
            val action =
                ChargingListFragmentDirections.actionChargingListFragmentToStationDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun calculateDistance(latitude: Double, longitude: Double): String {

        val result = FloatArray(1)
        Location.distanceBetween(
            lastLocation.latitude,
            lastLocation.longitude,
            latitude,
            longitude,
            result
        )

        return "%.1f".format(result[0] / 1000)
    }

    private fun checkAvailability(openingTime: String, closingTime: String): Boolean {
        val open = timeFormat.parse(openingTime)
        val close = timeFormat.parse(closingTime)
        val current = timeFormat.parse(currentTime)

        return current in open..close
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}