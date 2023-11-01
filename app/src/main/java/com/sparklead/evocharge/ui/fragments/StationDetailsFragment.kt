package com.sparklead.evocharge.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.HeroCarouselStrategy
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentStationDetailsBinding
import com.sparklead.evocharge.models.ChargingStation
import com.sparklead.evocharge.ui.adapters.CarouselAdapter
import com.sparklead.evocharge.ui.utils.CarouselItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationDetailsFragment : Fragment() {

    private var _binding: FragmentStationDetailsBinding? = null
    private val binding
        get() = _binding!!

    private val args: StationDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationDetailsBinding.inflate(inflater, container, false)

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.GONE

        val chargingStationDetails = args.chargingStation

        setupUI(chargingStationDetails)
        setUpCarousel(chargingStationDetails)

        return binding.root
    }

    private fun setupUI(details: ChargingStation) {

        binding.tvStationNameLocation.text = details.name
        binding.tvLocation.text = details.location
        binding.tvAvailableStatus.text =
            if (details.available) "Currently available" else "Unavailable"
        if (!details.available) binding.tvAvailableStatus.setTextColor(Color.RED)
        binding.tvDist.text = details.distance + " Km"
        binding.statusTime.text =
            if (details.available) "Open | ${details.openingTime} - ${details.closingTime}" else "Closed | ${details.openingTime} - ${details.closingTime}"
        binding.tvCompleteAddress.text = details.completeAddress
        binding.tvChargeType.text = details.chargeType + " DC"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNavigate.setOnClickListener {
            val uri = "google.navigation:q=${args.chargingStation.latitude},${args.chargingStation.longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
    }

    private fun setUpCarousel(details: ChargingStation) {

        val multiBrowseCenteredCarouselLayoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        binding.carouselRvAds.layoutManager = multiBrowseCenteredCarouselLayoutManager
        binding.carouselRvAds.isNestedScrollingEnabled = false

        val adapter = CarouselAdapter(
            object : CarouselAdapter.CarouselItemListener {
                override fun onItemClicked(item: CarouselItem, position: Int) {
                    binding.carouselRvAds.scrollToPosition(
                        position
                    )
                }
            }, R.layout.item_corosuel_station_image
        )
        val carouselItem: MutableList<CarouselItem> = mutableListOf()
        for (item in details.images) {
            carouselItem.add(CarouselItem(item))
        }
        binding.carouselRvAds.adapter = adapter
        adapter.submitList(carouselItem)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}