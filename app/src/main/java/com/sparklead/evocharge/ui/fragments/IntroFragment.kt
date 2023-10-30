package com.sparklead.evocharge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sparklead.evocharge.R
import com.sparklead.evocharge.databinding.FragmentIntroBinding
import com.sparklead.evocharge.models.OnboardingItem
import com.sparklead.evocharge.ui.adapters.IntroViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    private lateinit var onboardingAdapter: IntroViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        setOnboardingItem()
        return binding.root
    }

    private fun setOnboardingItem() {

        onboardingAdapter = IntroViewPagerAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.intro_one,
                    title = "Welcome To EvoCharge",
                    description = "A one-stop solution for finding and booking charging locations"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro_two,
                    title = "Dexa Scan Models",
                    description = "just ask your Career Doubt in live counsellor interaction"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro_three,
                    title = "Scan Qr and Pay Online",
                    description = "Explore numerous career options available at on place."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro_last,
                    title = "Google direction support",
                    description = "Explore numerous career options available at on place."
                )
            )
        )

        val onboardingViewPager = binding.onBoardingViewPager
        onboardingViewPager.adapter = onboardingAdapter
        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        binding.btnNext.setOnClickListener {
            if (onboardingViewPager.currentItem + 1 < onboardingAdapter.itemCount) {
                onboardingViewPager.currentItem += 1
            } else {
                findNavController().navigate(R.id.action_introFragment_to_authenticationActivity)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {

        binding.progressCircular.setProgress((((position + 1).toFloat()) / 4) * 100, true, 500L)
    }
}