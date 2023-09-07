package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sparklead.evocharge.databinding.FragmentCompleteProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteProfileBinding.inflate(inflater, container, false)

        return binding.root
    }
}