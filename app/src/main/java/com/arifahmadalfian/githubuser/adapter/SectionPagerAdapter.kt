package com.arifahmadalfian.githubuser.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arifahmadalfian.githubuser.FollowersFragment
import com.arifahmadalfian.githubuser.FollowingFragment
import com.arifahmadalfian.githubuser.R

class SectionPagerAdapter(
    private val mContext: Context,
    fm: FragmentManager
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var login: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.following, R.string.follower)

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
       return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = login?.let { FollowingFragment(it) }
            1 -> fragment = login?.let { FollowersFragment(it) }
        }
        return fragment as Fragment
    }
}