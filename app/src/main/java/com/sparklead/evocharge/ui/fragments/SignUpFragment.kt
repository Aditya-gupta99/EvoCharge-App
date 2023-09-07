package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sparklead.evocharge.databinding.FragmentSignUpBinding
import com.sparklead.evocharge.models.SignUpRequest
import com.sparklead.evocharge.service.AuthService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var service: AuthService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("@@@",
                service.signUpUser(SignUpRequest("Aditya", "Gupta", "divya12345@gmail.com", "paa"))
                    .toString()
            )
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}