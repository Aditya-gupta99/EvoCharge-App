package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentSignUpBinding
import com.sparklead.evocharge.service.AuthService
import com.sparklead.evocharge.ui.base.BaseFragment
import com.sparklead.evocharge.ui.states.SignUpUiState
import com.sparklead.evocharge.ui.utils.Constants
import com.sparklead.evocharge.ui.utils.PrefManager
import com.sparklead.evocharge.ui.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefManager: PrefManager

    @Inject
    lateinit var service: AuthService

    private lateinit var viewModel: SignUpViewModel

    private val passwordWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != null) {
                passwordValidation(p0.toString().trim { it <= ' ' })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            signUpUser()
        }

        binding.etPassword.addTextChangedListener(passwordWatcher)

        lifecycleScope.launch {
            viewModel.signUpUiState.collect{
                when(it){
                    is SignUpUiState.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    is SignUpUiState.Loading -> {
                        showLoadingDialog()
                    }
                    is SignUpUiState.Success -> {
                        hideLoading()
                        onSuccessfulSignup(it.message)
                    }
                    is SignUpUiState.Empty -> {

                    }
                }
            }
        }
    }

    private fun signUpUser() {
        if (validateUserDetails() && passwordValidation(
                binding.etPassword.text.toString().trim { it <= ' ' })
        ) {

            viewModel.signUpUser(binding.etFirstName.text.toString().trim { it <= ' ' },
                binding.etLastName.text.toString().trim { it <= ' ' },
                binding.etEmailName.text.toString().trim { it <= ' ' },
                binding.etPassword.text.toString().trim { it <= ' ' })
        }
    }

    private fun validateUserDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim { it <= ' ' }) -> {
                binding.userFirstName.error = "Please enter your first name"
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim { it <= ' ' }) -> {
                binding.userFirstName.error = null
                binding.userLastName.error = "Please enter your last name"
                false
            }

            TextUtils.isEmpty(binding.etEmailName.text.toString().trim { it <= ' ' }) -> {
                binding.userFirstName.error = null
                binding.userLastName.error = null
                binding.userEmail.error = "Please enter your email"
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmailName.text.toString().trim { it <= ' ' })
                .matches() -> {
                binding.userFirstName.error = null
                binding.userLastName.error = null
                binding.userEmail.error = "Please enter correct email"
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                binding.userFirstName.error = null
                binding.userLastName.error = null
                binding.userEmail.error = null
                binding.userPassword.error = "Please set your password"
                false
            }


            else -> {
                binding.userFirstName.error = null
                binding.userLastName.error = null
                binding.userEmail.error = null
                binding.userPassword.error = null
                true
            }
        }
    }

    private fun passwordValidation(p0: String): Boolean {
        if (!p0.matches(Regex(".*[0-9].*"))) {
            binding.userPassword.error = "Password should contain at least 1 digit"
            return false
        } else if (!p0.matches(Regex(".*[a-z].*"))) {
            binding.userPassword.error = "Password should contain at least 1 lower case letter"
            return false
        } else if (!p0.matches(Regex(".*[A-Z].*"))) {
            binding.userPassword.error = "Password should contain at least 1 upper case letter"
            return false
        } else if (!p0.matches(Regex(".*[a-zA-Z].*"))) {
            binding.userPassword.error = "Password should contain a letter"
            return false
        } else if (!p0.matches(Regex(".{8,}"))) {
            binding.userPassword.error = "Password should contain 8 characters"
            return false
        } else {
            binding.userPassword.error = null
            return true
        }
    }

    private fun onSuccessfulSignup(userId : String) {
        lifecycleScope.launch(Dispatchers.IO) {
            prefManager.saveStringValue(Constants.AUTH_STATUS,Constants.SIGNUP)
            prefManager.saveStringValue(Constants.USER_ID,userId)
        }
        findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}