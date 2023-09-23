package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentCompleteProfileBinding
import com.sparklead.evocharge.models.User
import com.sparklead.evocharge.ui.base.BaseFragment
import com.sparklead.evocharge.ui.states.CompleteProfileUiState
import com.sparklead.evocharge.ui.utils.Constants
import com.sparklead.evocharge.ui.utils.PrefManager
import com.sparklead.evocharge.ui.viewmodels.CompleteProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompleteProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentCompleteProfileBinding
    private lateinit var viewModel: CompleteProfileViewModel

    @Inject
    lateinit var prefManager: PrefManager

    private lateinit var userId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CompleteProfileViewModel::class.java]

        val arrayList = arrayListOf("Ather", "Ola", "TVS", "Hero", "Bajaj", "Vida", "Yulu")
        binding.etBrandName.setSimpleItems(arrayList.toTypedArray())

        val atherList =
            arrayListOf("450", "450 Plus Gen 2", "450x Gen 2", "450x Gen 3", "450 Plus Gen 3")
        binding.etModelName.setSimpleItems(atherList.toTypedArray())


        lifecycleScope.launch(Dispatchers.IO) {
            prefManager.readStringValue(Constants.USER_ID).collect {
                userId = it
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.completeProfileUiState.collect {
                when(it){
                    is CompleteProfileUiState.Empty -> {}
                    is CompleteProfileUiState.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                    }
                    is CompleteProfileUiState.Loading -> {
                        showLoadingDialog()
                    }
                    is CompleteProfileUiState.Success -> {
                        hideLoading()
                        findNavController().navigate(R.id.action_completeProfileFragment_to_welcomeFragment)
                    }
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (validateInput()) {
                viewModel.updateUser(userId, User().apply {
                    brand = binding.etBrandName.text.toString().trim { it <= ' ' }
                    model = binding.etModelName.text.toString().trim { it <= ' ' }
                })
            }
        }
    }

    private fun validateInput(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etBrandName.text.toString().trim { it <= ' ' }) -> {
                binding.brandName.error = "Please select your Vehicle Brand"
                return false
            }

            TextUtils.isEmpty(binding.etModelName.text.toString().trim { it <= ' ' }) -> {
                binding.brandName.error = null
                binding.modelName.error = "Please select your Vehicle model"
                return false
            }

            else -> {
                binding.brandName.error = null
                binding.modelName.error = null
                return true
            }
        }
    }
}