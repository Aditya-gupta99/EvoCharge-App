package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentSplashBinding
import com.sparklead.evocharge.ui.utils.Constants
import com.sparklead.evocharge.ui.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        checkAuthStage()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGettingStarted.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_introFragment)
        }
    }

    private fun checkAuthStage(){
        lifecycleScope.launch(Dispatchers.IO) {
            prefManager.readStringValue(Constants.AUTH_STATUS).collect{
                Log.e("@@@",it)
                when(it) {
                    "" ->{
                        binding.btnGettingStarted.visibility = View.VISIBLE
                    }
                    Constants.SIGNUP -> {
                        nextActivity()
                    }
                    Constants.COMPLETE_PROFILE -> {
                        nextActivity()
                    }
                }
            }
        }
    }

    private fun nextActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_authenticationActivity)
        }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}