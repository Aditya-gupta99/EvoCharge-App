package com.sparklead.evocharge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        UltimateBarX.statusBar(this)
            .transparent()
            .light(true)
            .apply()
    }
}