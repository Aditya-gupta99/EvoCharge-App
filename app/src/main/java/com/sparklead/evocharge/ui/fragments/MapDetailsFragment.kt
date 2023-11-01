package com.sparklead.evocharge.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentMapDetailsBinding
import com.sparklead.evocharge.models.ChargingStation
import com.sparklead.evocharge.models.ChargingStationResponse
import com.sparklead.evocharge.ui.base.BaseFragment
import com.sparklead.evocharge.ui.states.MapDetailUiState
import com.sparklead.evocharge.ui.utils.Constants
import com.sparklead.evocharge.ui.viewmodels.MapDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MapDetailsFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapDetailsBinding? = null
    val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markerLocation: LatLng
    private lateinit var viewModel: MapDetailViewModel
    private lateinit var closestStation: ChargingStation
    private lateinit var timeFormat: DateFormat
    private lateinit var currentTime: String
    private lateinit var stationList: ArrayList<ChargingStation>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MapDetailViewModel::class.java]

        initializeMap(savedInstanceState)

        viewModel.getChargingList()

        timeFormat = SimpleDateFormat("hh:mm aaa", Locale.US)
        currentTime = timeFormat.format(Date())

        bottomSheetState()

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.GONE

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.mapDetailUiState.collect {
                when (it) {
                    is MapDetailUiState.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    is MapDetailUiState.Loading -> {
                        showLoadingDialog()
                    }

                    is MapDetailUiState.Success -> {
                        hideLoading()
                        mappingList(it.list)
                    }
                }
            }
        }


        binding.getCurrentLocation.setOnClickListener {

            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                lastLocation = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 14f))
            }
            updateBottomSheet(closestStation, true)
        }

        binding.btnNavigate.setOnClickListener {

            val uri = "google.navigation:q=${markerLocation.latitude},${markerLocation.longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }


    }

    @SuppressLint("MissingPermission")
    private fun initializeMap(savedInstanceState: Bundle?) {

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        MapsInitializer.initialize(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        Places.initialize(requireContext(), Constants.MAP_SECRET_KEY)

        fusedLocationClient.lastLocation.addOnSuccessListener {
            lastLocation = LatLng(it.latitude, it.longitude)
        }
    }

    private fun mappingList(list: ArrayList<ChargingStationResponse>) {

        val chargingList: ArrayList<ChargingStation> = ArrayList()
        list.forEach {

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

        val temp = chargingList.sortedBy { it.distance }
        closestStation = temp[0]

        updateBottomSheet(closestStation, true)

        stationList = chargingList
        placeMarker(chargingList)
    }

    private fun checkAvailability(openingTime: String, closingTime: String): Boolean {
        val open = timeFormat.parse(openingTime)
        val close = timeFormat.parse(closingTime)
        val current = timeFormat.parse(currentTime)

        return current in open..close
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

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setOnMarkerClickListener(this)
        binding.mapView.overlay.remove(binding.mapView)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_REQUEST_CODE
            )

            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

            if (location != null) {
                val currentLatLong = LatLng(location.latitude, location.longitude)
                markerLocation = currentLatLong
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 14f))
            }
        }
    }

    private fun placeMarker(list: ArrayList<ChargingStation>) {

        list.forEach {
            val markerOptions = MarkerOptions().position(LatLng(it.latitude, it.longitude))
                .icon(getBitmapDescriptorFromVector(requireContext(), R.drawable.available_marker))
            mMap.addMarker(markerOptions)
        }

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
        val markerStation =
            stationList.filter { it.latitude == marker.position.latitude && it.longitude == marker.position.longitude }
        updateBottomSheet(markerStation[0], false)
        markerLocation = marker.position
        return true
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
    }

    private fun getBitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResId: Int
    ): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResId)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun bottomSheetState() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 420
        bottomSheetBehavior.isHideable = false

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
    }

    private fun updateBottomSheet(station: ChargingStation, close: Boolean) {
        if (close) binding.tvClosest.visibility = View.VISIBLE else binding.tvClosest.visibility =
            View.INVISIBLE
        binding.tvStationName.text = station.name
        binding.tvLocation.text = station.location
        binding.tvAvailableStatus.text =
            if (station.available) "Currently available" else "Unavailable"
        if (!station.available) binding.tvAvailableStatus.setTextColor(Color.RED) else binding.tvAvailableStatus.setTextColor(
            resources.getColor(
                com.google.android.libraries.places.R.color.quantum_googgreen
            )
        )
        binding.tvDist.text = station.distance + " Km"
        binding.tvStatusTime.text =
            if (station.available) "Open | ${station.openingTime} - ${station.closingTime}" else "Closed | ${station.openingTime} - ${station.closingTime}"
        binding.tvCompleteAddress.text = station.completeAddress
        binding.tvChargeType.text = station.chargeType + " DC"
    }
}