package io.github.vnicius.twitterclone.ui.profile

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(val manager: FragmentManager): FragmentPagerAdapter(manager) {
    var mFragmentsList: MutableList<Fragment> = mutableListOf()
    var mFragmentsTitlesList: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return mFragmentsList[position]
    }

    override fun getCount(): Int {
        return mFragmentsList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentsList.add(fragment)
        mFragmentsTitlesList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentsTitlesList[position]
    }
}