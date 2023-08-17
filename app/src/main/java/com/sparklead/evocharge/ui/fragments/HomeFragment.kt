package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.android.material.sidesheet.SideSheetDialog
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding ? =null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.notificationIcon.setOnClickListener {
            showSheet()
        }
        return binding.root
    }

    private fun showSheet(){
        val sideSheetDialog = SideSheetDialog(requireContext())

        sideSheetDialog.behavior.addCallback(object : SideSheetCallback() {
            override fun onStateChanged(sheet: View, newState: Int) {
                if (newState == SideSheetBehavior.STATE_DRAGGING) {
                    sideSheetDialog.behavior.state = SideSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(sheet: View, slideOffset: Float) {
            }
        })

        val inflater = layoutInflater.inflate(R.layout.side_sheet_notification,null)
        val btnClose = inflater.findViewById<ImageButton>(R.id.btn_close)

        btnClose.setOnClickListener {
            sideSheetDialog.dismiss()
        }

        sideSheetDialog.setCancelable(false)
        sideSheetDialog.setCanceledOnTouchOutside(true)
        sideSheetDialog.setContentView(inflater)
        sideSheetDialog.show()
    }
}