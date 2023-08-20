package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentChargingListBinding

class ChargingListFragment : Fragment() {

    private var _binding : FragmentChargingListBinding ? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChargingListBinding.inflate(inflater,container,false)


        return binding.root
    }
}