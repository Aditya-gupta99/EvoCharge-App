package com.sparklead.evocharge.ui.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Constants {

    const val LOCATION_REQUEST_CODE = 1
    const val AUTH_STATUS = "auth_status"
    const val USER_ID = "user_id"
    const val USER = "user"
    const val SIGNUP = "signup"
    const val COMPLETE_PROFILE = "complete_profile"
    const val MAP_SECRET_KEY = "AIzaSyAUCICDqqPIkZnVq0vimeCYZqtYltXZ-AU"

    fun appSettingOpen(context: Context){
        Toast.makeText(
            context,
            "Go to Setting and Enable All Permission",
            Toast.LENGTH_LONG
        ).show()

        val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        settingIntent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(settingIntent)
    }

    fun warningPermissionDialog(context: Context, listener : DialogInterface.OnClickListener){
        MaterialAlertDialogBuilder(context)
            .setMessage("All Permission are Required for this app")
            .setCancelable(false)
            .setPositiveButton("Ok",listener)
            .create()
            .show()
    }
}