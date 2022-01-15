package com.example.messengerapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.messengerapp.R
import com.example.messengerapp.databinding.FragmentProfileBinding
import com.example.messengerapp.di.profile.DaggerProfileComponent
import com.example.messengerapp.di.profile.ProfileComponent
import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.presentation.elm.profile.model.ProfileCommand
import com.example.messengerapp.presentation.elm.profile.model.ProfileEffect
import com.example.messengerapp.presentation.elm.profile.model.ProfileEvent
import com.example.messengerapp.presentation.elm.profile.model.ProfileState
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import com.example.messengerapp.util.ext.appComponent
import com.example.messengerapp.util.ext.fastLazy
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private val user by fastLazy { requireArguments().getParcelable(USER_KEY) as? UserUi }

    private var _binding: FragmentProfileBinding? = null
    val binding: FragmentProfileBinding
        get() = _binding!!

    private lateinit var profileComponent: ProfileComponent

    @Inject
    lateinit var profileStore: ElmStore<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>

    @Inject
    lateinit var authorizedUserUi: UserUi

    override fun onAttach(context: Context) {
        profileComponent = DaggerProfileComponent.builder()
            .appComponent(context.appComponent)
            .build()
        profileComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initUi()

        return binding.root
    }

    private fun initUi() {
        if (user == null) {
            store.accept(ProfileEvent.Ui.GetStatus)
            binding.userDetailsAppBar.visibility = View.GONE
            setUserDetails(authorizedUserUi)
        } else {
            binding.userDetailsAppBar.visibility = View.VISIBLE
            (activity as AppCompatActivity).setSupportActionBar(binding.userDetailToolbar)
            binding.userDetailToolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            setUserDetails(user!!)
        }
    }

    private fun setStatus(status: UserStatus) {
        binding.userDetailsOnlineStatus.text = when (status) {
            UserStatus.ACTIVE -> "active"
            UserStatus.IDLE -> "idle"
            UserStatus.OFFLINE -> "offline"
        }
        binding.userDetailsOnlineStatus.setTextColor(
            when (status) {
                UserStatus.ACTIVE -> resources.getColor(R.color.active_status_color, null)
                UserStatus.IDLE -> resources.getColor(R.color.idle_status_color, null)
                UserStatus.OFFLINE -> resources.getColor(R.color.offline_status_color, null)
            }
        )
    }

    private fun setUserDetails(user: UserUi) {
        loadImage(user.avatarUrl)
        setStatus(user.status)
        binding.userDetailsUsername.text = user.name
    }

    private fun loadImage(url: String?) {
        val avatar = binding.userAvatar
        if (url != null) {
            avatar.load(url) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
                transformations(
                    RoundedCornersTransformation(
                        10f,
                        10f,
                        10f,
                        10f
                    )
                )
            }
        } else {
            avatar.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.mipmap.ic_launcher
                )
            )
        }
    }

    override val initEvent: ProfileEvent
        get() = ProfileEvent.Ui.Init

    override fun createStore(): Store<ProfileEvent, ProfileEffect, ProfileState> = profileStore

    override fun render(state: ProfileState) = when (state) {
        is ProfileState.Error -> {
        }
        ProfileState.Loading -> {
        }
        is ProfileState.Success -> {
            setStatus(state.status)
        }
    }

    companion object {

        private const val USER_KEY = "USER"

        fun newInstance(user: UserUi? = null): ProfileFragment {
            return ProfileFragment().apply {
                arguments = bundleOf(
                    USER_KEY to user
                )
            }
        }
    }

}