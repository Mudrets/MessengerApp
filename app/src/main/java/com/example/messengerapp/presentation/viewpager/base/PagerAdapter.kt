package com.example.messengerapp.presentation.viewpager.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

open class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : BasePagerAdapter(fragmentManager, lifecycle) {

    private val localFragments = mutableListOf<Fragment>()

    override var fragments: List<Fragment>
        get() = localFragments
        set(newFragments) {
            localFragments.clear()
            localFragments.addAll(newFragments)
            notifyDataSetChanged()
        }

}