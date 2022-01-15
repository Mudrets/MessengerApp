package com.example.messengerapp

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.messengerapp.databinding.ActivityMainBinding
import com.example.messengerapp.di.activity.ActivityComponent
import com.example.messengerapp.di.activity.DaggerActivityComponent
import com.example.messengerapp.presentation.elm.main.MainStoreFactory
import com.example.messengerapp.presentation.elm.main.model.MainEffect
import com.example.messengerapp.presentation.elm.main.model.MainEvent
import com.example.messengerapp.presentation.elm.main.model.MainState
import com.example.messengerapp.presentation.fragment.BottomNavFragment
import com.example.messengerapp.presentation.view_group.message.EmojiUi
import com.example.messengerapp.util.ext.appComponent
import timber.log.Timber
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class MainActivity : ElmActivity<MainEvent, MainEffect, MainState>() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    lateinit var activityComponent: ActivityComponent

    @Inject
    lateinit var storeFactory: MainStoreFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerActivityComponent
            .builder()
            .appComponent(appComponent)
            .build()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.activityFragmentContainer, BottomNavFragment(), "tag")
                .commit()
        }
    }

    private fun onLoading() {
        binding.activityFragmentContainer.isVisible = false
        binding.loadingText.isVisible = true
        binding.loadingProgressBar.isVisible = true
    }

    private fun onSuccess() {
        binding.activityFragmentContainer.isVisible = true
        binding.loadingText.isVisible = false
        binding.loadingProgressBar.isVisible = false
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override val initEvent: MainEvent = MainEvent.Ui.GetUserInfo

    override fun createStore(): Store<MainEvent, MainEffect, MainState> = storeFactory.provide()

    override fun render(state: MainState) = when (state) {
        is MainState.Error -> {
            Timber.e(state.th)
            Toast.makeText(this, getString(R.string.common_error_title), Toast.LENGTH_SHORT).show()
        }
        MainState.Loading -> onLoading()
        is MainState.SuccessUserInfo -> {
            onSuccess()
            store.accept(MainEvent.Ui.GetEmojiData)
        }
        is MainState.SuccessEmojiData -> {
            EmojiUi.emojiInfo = state.emojiInfo
        }
    }
}