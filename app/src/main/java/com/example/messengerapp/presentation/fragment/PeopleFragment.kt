package com.example.messengerapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.databinding.FragmentPeopleBinding
import com.example.messengerapp.di.people.DaggerPeopleComponent
import com.example.messengerapp.di.people.PeopleComponent
import com.example.messengerapp.presentation.elm.people.model.PeopleCommand
import com.example.messengerapp.presentation.elm.people.model.PeopleEffect
import com.example.messengerapp.presentation.elm.people.model.PeopleEvent
import com.example.messengerapp.presentation.elm.people.model.PeopleState
import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.user.PeopleShimmer
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import com.example.messengerapp.util.Constants
import com.example.messengerapp.util.ext.appComponent
import com.example.messengerapp.util.ext.fastLazy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class PeopleFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleState>() {

    private val resultKey by fastLazy { requireArguments().getString(ARG_RESULT_KEY, "") }

    private var _binding: FragmentPeopleBinding? = null
    val binding: FragmentPeopleBinding
        get() = _binding!!

    private var lastSearchQuery: String = Constants.INITIAL_QUERY

    private lateinit var peopleComponent: PeopleComponent

    @Inject
    lateinit var searchUserSubject: PublishSubject<String>

    @Inject
    lateinit var peopleStore: ElmStore<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>

    @Inject
    lateinit var userSubject: PublishSubject<UserUi>

    @Inject
    lateinit var userStatusSubject: PublishSubject<Long>

    @Inject
    lateinit var adapter: AsyncAdapter<ViewTyped>

    @Inject
    lateinit var layoutManager: Provider<RecyclerView.LayoutManager>

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context) {
        peopleComponent = DaggerPeopleComponent.builder()
            .appComponent(context.appComponent)
            .build()
        peopleComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSubject
            .subscribeBy { userUi ->
                setFragmentResult(resultKey, bundleOf("user" to userUi))
            }.addTo(compositeDisposable)

        userStatusSubject
            .subscribeBy { userId ->
                store.accept(PeopleEvent.Ui.GetUserStatus(userId))
            }.addTo(compositeDisposable)

        searchUserSubject
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribeBy { searchQuery ->
                lastSearchQuery = searchQuery
                store.accept(PeopleEvent.Ui.SearchUsers(searchQuery))
            }.addTo(compositeDisposable)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)

        initUi()

        return binding.root
    }

    private fun showLoad() {
        normalState()
        adapter.items = generateSequence { PeopleShimmer() }.take(10).toList()
    }

    private fun initUi() {
        binding.apply {
            peopleRecyclerView.adapter = adapter
            peopleRecyclerView.layoutManager = layoutManager.get()
            searchPeopleEditText.doAfterTextChanged { editable ->
                val searchQuery: String = editable.toString()
                searchUserSubject.onNext(searchQuery)
            }
            reloadPeopleButton.setOnClickListener {
                store.accept(PeopleEvent.Ui.SearchUsers(lastSearchQuery))
            }
        }
    }

    private fun normalState() {
        binding.apply {
            peopleRecyclerView.isVisible = true
            emptyPeopleListImage.isVisible = false
            errorPeopleImage.isVisible = false
            emptyPeopleListText.isVisible = false
            peopleErrorText.isVisible = false
            reloadPeopleButton.isVisible = false
        }
    }

    private fun showError() {
        normalState()
        binding.apply {
            peopleRecyclerView.isVisible = false
            errorPeopleImage.isVisible = true
            peopleErrorText.isVisible = true
            reloadPeopleButton.isVisible = true
        }
    }

    private fun showEmptyListWarning() {
        normalState()
        binding.apply {
            peopleRecyclerView.isVisible = false
            emptyPeopleListImage.isVisible = true
            emptyPeopleListText.isVisible = true
        }
    }

    override val initEvent: PeopleEvent = PeopleEvent.Ui.GetUsers

    override fun createStore(): Store<PeopleEvent, PeopleEffect, PeopleState> = peopleStore

    override fun render(state: PeopleState) = when (state) {
        is PeopleState.Error -> {
            if (adapter.items.indexOfFirst { it is UserUi } == -1)
                showError()
            else
                Unit
        }
        PeopleState.Loading -> showLoad()
        is PeopleState.Success -> {
            normalState()
            if (state.users.isEmpty())
                showEmptyListWarning()
            adapter.items = state.users
        }
    }

    override fun onDestroy() {
        _binding = null
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {

        private const val ARG_RESULT_KEY = "ARG_RESULT_KEY"

        fun newInstance(resultKey: String): Fragment {
            return PeopleFragment().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                )
            }
        }
    }
}