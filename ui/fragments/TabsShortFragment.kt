package com.peopleperfectae.ui.common


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.peopleperfectae.data.SharedData
import com.peopleperfectae.databinding.FragmentTabsShortBinding
import com.peopleperfectae.utils.setMarginTop


abstract class TabsShortFragment : Fragment() {
    protected lateinit var binding: FragmentTabsShortBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabsShortBinding.inflate(inflater, container, false)

        initToolbar(getAdapter())

        setTitle()

        return binding.root
    }

    private fun initToolbar(pagerAdapter: BasePagerAdapter){
        val tabLayout = binding.tabs
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = pagerAdapter.getTabTitle(position)
        }.attach()

        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount

        setCurrentTab(SharedData.currentTab)

        //set margin to toolbar
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { _, insets ->
            binding.toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
    }

    private fun setCurrentTab(pos: Int) {
        binding.viewPager.currentItem = pos
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedData.currentTab = 0   // reset the current tab to zero after destroying fragment
    }

    abstract fun getAdapter() : BasePagerAdapter

    abstract fun setTitle()

}
