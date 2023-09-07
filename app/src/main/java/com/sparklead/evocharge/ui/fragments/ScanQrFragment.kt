package com.sparklead.evocharge.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentScanQrBinding
import com.sparklead.evocharge.ui.utils.Constants.appSettingOpen
import com.sparklead.evocharge.ui.utils.Constants.warningPermissionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanQrFragment : Fragment() {

    private var _binding: FragmentScanQrBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var codeScanner: CodeScanner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrBinding.inflate(inflater, container, false)

        permissionCheck()

        codeScannerSetup()

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.GONE

        return binding.root
    }

    private fun codeScannerSetup() {
        codeScanner = CodeScanner(requireContext(), binding.scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false



        codeScanner.decodeCallback = DecodeCallback {
            // TODO after scan complete
        }
        codeScanner.errorCallback = ErrorCallback {
            Toast.makeText(
                requireContext(), "Camera initialization error: ${it.message}",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun permissionCheck() {
        val permission = Manifest.permission.CAMERA
        permissionLauncher.launch(permission)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Permission Granted...", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Permission denied...", Toast.LENGTH_LONG).show()
            warningPermissionDialog(requireContext()) { _: DialogInterface, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        appSettingOpen(requireContext())
                }
            }
        }
        return@registerForActivityResult
    }
}