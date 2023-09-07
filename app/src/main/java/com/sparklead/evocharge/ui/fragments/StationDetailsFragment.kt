package com.sparklead.evocharge.ui.fragments

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

        val chargingStation = args.chargingStation

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

        val cor: MutableList<CarouselItem> = mutableListOf()
        cor.add(CarouselItem("https://i.postimg.cc/13sK3Yrz/Component-1-4.png", R.string.app_name))
        cor.add(
            CarouselItem(
                "https://firebasestorage.googleapis.com/v0/b/vepaso-75c35.appspot.com/o/Component%202%20(3).png?alt=media&token=2be17462-832c-4992-984d-10e64b57114f",
                R.string.app_name
            )
        )

        binding.carouselRvAds.adapter = adapter
        adapter.submitList(cor)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}