package com.printoverit.cookbookupdate.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
   private var resultBundle: Bundle,
   private var fragment:ArrayList<Fragment>,
    fragmentActivity: FragmentActivity
) :FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return fragment.size
    }

    override fun createFragment(position: Int): Fragment {
        fragment[position].arguments=resultBundle
        return fragment[position]
    }
}