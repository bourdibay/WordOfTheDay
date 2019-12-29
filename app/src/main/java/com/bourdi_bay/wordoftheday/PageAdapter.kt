package com.bourdi_bay.wordoftheday

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        if (position == 1) {
            return BasicListFragment()
        }
        return IntermediateListFragment()
    }

    override fun getCount(): Int {
        return 2
    }
}