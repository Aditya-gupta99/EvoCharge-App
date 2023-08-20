package com.sparklead.evocharge.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentStationsDetailsDialogBinding

class StationsDetailsDialogFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentStationsDetailsDialogBinding ? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationsDetailsDialogBinding.inflate(inflater,container,false)

        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}