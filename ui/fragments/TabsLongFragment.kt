package com.peopleperfectae.ui.common


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.peopleperfectae.databinding.FragmentTabsLongBinding
import com.peopleperfectae.utils.setMarginTop


abstract class TabsLongFragment : Fragment() {
    protected lateinit var binding: FragmentTabsLongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabsLongBinding.inflate(inflater, container, false)

        initTabs(getAdapter())
        setTitle()

        return binding.root
    }

    private fun initTabs(pagerAdapter: BasePagerAdapter){
        val tabLayout = binding.tabs
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = pagerAdapter.getTabTitle(position)
        }.attach()

        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount

        //set margin to toolbar
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { _, insets ->
            binding.toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
    }

    abstract fun setTitle()

    abstract fun getAdapter() : BasePagerAdapter

}
