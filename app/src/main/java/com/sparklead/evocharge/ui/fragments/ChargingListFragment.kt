package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentChargingListBinding
import com.sparklead.evocharge.models.ChargingStation
import com.sparklead.evocharge.ui.adapters.StationListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChargingListFragment : Fragment() {

    private var _binding: FragmentChargingListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChargingListBinding.inflate(inflater, container, false)

        val stationList = mutableListOf(
            ChargingStation("Aditya Gupta", "ks Layout", true),
            ChargingStation("New Center", "Bangalore", true),
            ChargingStation("Coffee Park", "ks Layout", false),
            ChargingStation("Meghanas", "hore", true)
        )

        val adapter = StationListAdapter(stationList)
        binding.rvChargingStation.adapter = adapter
        binding.rvChargingStation.layoutManager = LinearLayoutManager(requireContext())

        adapter.onItemClick = {
            val action =
                ChargingListFragmentDirections.actionChargingListFragmentToStationDetailsFragment(it)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
    }
}