package com.example.messengerapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.messengerapp.R
import com.example.messengerapp.databinding.FragmentBottomNavBinding
import com.example.messengerapp.presentation.fragment.chat.ChatFragment
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import timber.log.Timber

class BottomNavFragment : Fragment() {

    private var _binding: FragmentBottomNavBinding? = null
    val binding: FragmentBottomNavBinding
        get() = _binding!!

    private var launchedFragmentName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomNavBinding.inflate(inflater, container, false)
        activity?.window?.statusBarColor = resources.getColor(R.color.search_channels_app_bar, null)

        if (savedInstanceState != null && savedInstanceState.containsKey(LAUNCHED_FRAGMENT_NAME_KEY))
            launchedFragmentName = savedInstanceState.getString(LAUNCHED_FRAGMENT_NAME_KEY)

        if (childFragmentManager.backStackEntryCount > 0)
            childFragmentManager.popBackStack()
        when (launchedFragmentName) {
            PROFILE_FRAGMENT_TAG -> {
                launchFragmentWithBottomMenu(
                    ProfileFragment.newInstance(),
                    PROFILE_FRAGMENT_TAG
                )
            }
            PEOPLE_FRAGMENT_TAG -> {
                launchFragmentWithBottomMenu(
                    PeopleFragment.newInstance(PEOPLE_RES_KEY),
                    PEOPLE_FRAGMENT_TAG
                )
            }
            else -> {
                launchFragmentWithBottomMenu(
                    ChannelsFragment.newInstance(CHANNELS_RES_KEY),
                    CHANNEL_FRAGMENT_TAG
                )
            }
        }

        setBottomNavMenuListener()

        return binding.root
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.activityFragmentContainer, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun launchFragmentWithBottomMenu(fragment: Fragment, tag: String) {
        childFragmentManager.beginTransaction()
            .replace(R.id.bottomNavFragmentContainer, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun setBottomNavMenuListener() {
        binding.bottomNavigationMenu.setOnItemSelectedListener { menuItem ->
            Timber.e("Backstack size: ${childFragmentManager.backStackEntryCount}")
            childFragmentManager.popBackStack()
            when (menuItem.itemId) {
                R.id.channels -> {
                    launchedFragmentName = CHANNEL_FRAGMENT_TAG
                    launchFragmentWithBottomMenu(
                        ChannelsFragment.newInstance(CHANNELS_RES_KEY),
                        CHANNEL_FRAGMENT_TAG
                    )
                }
                R.id.people -> {
                    launchedFragmentName = PEOPLE_FRAGMENT_TAG
                    launchFragmentWithBottomMenu(
                        PeopleFragment.newInstance(PEOPLE_RES_KEY),
                        PEOPLE_FRAGMENT_TAG
                    )
                }
                R.id.profile -> {
                    launchedFragmentName = PROFILE_FRAGMENT_TAG
                    launchFragmentWithBottomMenu(
                        ProfileFragment.newInstance(),
                        PROFILE_FRAGMENT_TAG
                    )
                }
            }
            true
        }
    }

    private fun setFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(
            CHANNELS_RES_KEY,
            this
        ) { _, bundle ->
            val streamName = bundle.getString("streamName") ?: ""
            val topicName = bundle.getString("topicName") ?: ""
            val streamId = bundle.getLong("streamId")
            launchFragment(
                ChatFragment.newInstance(
                    streamName,
                    streamId,
                    topicName,
                    CHAT_RES_KEY
                ), "ChatFragment"
            )
        }

        childFragmentManager.setFragmentResultListener(
            PEOPLE_RES_KEY,
            this
        ) { _, bundle ->
            val userUi = bundle.getParcelable("user") as? UserUi
            if (userUi != null) {
                launchFragment(ProfileFragment.newInstance(userUi), "AnotherProfileFragment")
            }
        }

        parentFragmentManager.setFragmentResultListener(
            CHAT_RES_KEY,
            activity!!
        ) { _, bundle ->
            val streamName = bundle.getString("streamName") ?: ""
            val topicName = bundle.getString("topicName") ?: ""
            val streamId = bundle.getLong("streamId")
            launchFragment(
                ChatFragment.newInstance(
                    streamName,
                    streamId,
                    topicName
                ), "ChatFragment"
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LAUNCHED_FRAGMENT_NAME_KEY, launchedFragmentName)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {

        private const val CHANNELS_RES_KEY = "CHANNELS_RES"
        private const val PEOPLE_RES_KEY = "PEOPLE_RES"
        private const val CHAT_RES_KEY = "CHAT_RES_KEY"
        private const val PROFILE_FRAGMENT_TAG = "Profile Fragment"
        private const val PEOPLE_FRAGMENT_TAG = "People Fragment"
        private const val CHANNEL_FRAGMENT_TAG = "Channel Fragment"
        private const val LAUNCHED_FRAGMENT_NAME_KEY = "Launched fragment name"
    }

}