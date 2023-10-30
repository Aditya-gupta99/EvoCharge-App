package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentSignInBinding
import com.sparklead.evocharge.models.User
import com.sparklead.evocharge.ui.base.BaseFragment
import com.sparklead.evocharge.ui.states.SignInUiState
import com.sparklead.evocharge.ui.utils.PrefManager
import com.sparklead.evocharge.ui.viewmodels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding
        get() = _binding!!

    private lateinit var viewModel: SignInViewModel

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.btnRegister.setOnClickListener {
            if (validateInput()) {
                viewModel.signInUser(
                    binding.etEmailName.text.toString().trim { it <= ' ' },
                    binding.etPassword.text.toString().trim { it <= ' ' })
            }
        }

        lifecycleScope.launch {
            viewModel.signInUiState.collect {
                when(it) {
                    is SignInUiState.Empty -> {}
                    is SignInUiState.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    SignInUiState.Loading -> {
                        showLoadingDialog()
                    }
                    is SignInUiState.Success -> {
                        hideLoading()
                        Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                        signInSuccess(it.user)
                    }
                }
            }
        }
    }

    private suspend fun signInSuccess(user: User) {
        prefManager.saveUser(user)
        findNavController().navigate(R.id.action_signInFragment_to_welcomeFragment)
    }

    private fun validateInput(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmailName.text.toString().trim { it <= ' ' }) -> {
                binding.userEmail.error = "Enter email."
                return false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                binding.userEmail.error = null
                binding.userPassword.error = "Enter password."
                return false
            }

            else -> {
                binding.etEmailName.error = null
                binding.userPassword.error = null
                return true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}