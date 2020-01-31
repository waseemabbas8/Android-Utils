package com.peopleperfectae.ui.common


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.peopleperfectae.databinding.FragmentSubTabsLongBinding


abstract class SubTabsLongFragment : Fragment() {
    private lateinit var binding: FragmentSubTabsLongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubTabsLongBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        initTabs(getAdapter())

        return binding.root
    }

    private fun initTabs(pagerAdapter: BasePagerAdapter){
        val tabLayout = binding.tabs
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = pagerAdapter.getTabTitle(position)
        }.attach()

        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount
    }

    abstract fun getAdapter() : BasePagerAdapter

}
