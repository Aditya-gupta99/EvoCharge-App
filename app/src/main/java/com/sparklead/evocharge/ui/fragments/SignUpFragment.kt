package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.sparklead.evocharge.databinding.FragmentSignUpBinding
import com.sparklead.evocharge.models.SignUpRequest
import com.sparklead.evocharge.service.AuthService
import com.sparklead.evocharge.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var service: AuthService

    private var passwordValidate = false

    private val passwordWatcher = object :TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(p0 != null){
                passwordValidation(p0.toString())
//                if (!p0.matches(Regex(".*[0-9].*"))) {
//                    binding.userPassword.error = "Password should contain at least 1 digit"
//                    passwordValidate = false
//                }
//                else if (!p0.matches(Regex(".*[a-z].*"))) {
//                    binding.userPassword.error = "Password should contain at least 1 lower case letter"
//                    passwordValidate = false
//                }
//                else if (!p0.matches(Regex(".*[A-Z].*"))) {
//                    binding.userPassword.error = "Password should contain at least 1 upper case letter"
//                    passwordValidate = false
//                }
//                else if (!p0.matches(Regex(".*[a-zA-Z].*"))) {
//                    binding.userPassword.error = "Password should contain a letter"
//                    passwordValidate = false
//                }
//                else if (!p0.matches( Regex(".{8,}"))) {
//                    binding.userPassword.error = "Password should contain 8 characters"
//                    passwordValidate = false
//                }
//                else {
//                    passwordValidate = true
//                    binding.userPassword.error = null
//                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            signUpUser()
        }

        binding.etPassword.addTextChangedListener(passwordWatcher)
    }

    private fun signUpUser() {
        if (validateUserDetails() && passwordValidation(binding.etPassword.text.toString().trim { it <= ' ' })) {
            showLoadingDialog()


            CoroutineScope(Dispatchers.IO).launch {
                Log.d(
                    "@@@",
                    service.signUpUser(
                        SignUpRequest(
                            binding.etFirstName.text.toString().trim { it <= ' ' },
                            binding.etLastName.text.toString().trim { it <= ' ' },
                            binding.etEmailName.text.toString().trim { it <= ' ' },
                            binding.etPassword.text.toString().trim { it <= ' ' }
                        )
                    ).toString()
                )
                hideLoading()
            }
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

            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmailName.text.toString().trim { it <= ' ' }).matches() -> {
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

    private fun passwordValidation(p0 : String) : Boolean{
        if (!p0.matches(Regex(".*[0-9].*"))) {
            binding.userPassword.error = "Password should contain at least 1 digit"
//            passwordValidate = false
            return false
        }
        else if (!p0.matches(Regex(".*[a-z].*"))) {
            binding.userPassword.error = "Password should contain at least 1 lower case letter"
//            passwordValidate = false
            return false
        }
        else if (!p0.matches(Regex(".*[A-Z].*"))) {
            binding.userPassword.error = "Password should contain at least 1 upper case letter"
            passwordValidate = false
            return false
        }
        else if (!p0.matches(Regex(".*[a-zA-Z].*"))) {
            binding.userPassword.error = "Password should contain a letter"
            passwordValidate = false
            return false
        }
        else if (!p0.matches( Regex(".{8,}"))) {
            binding.userPassword.error = "Password should contain 8 characters"
            passwordValidate = false
            return false
        }
        else {
            passwordValidate = true
            binding.userPassword.error = null
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}