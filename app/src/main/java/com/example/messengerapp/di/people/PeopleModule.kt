package com.example.messengerapp.di.people

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.di.annotation.scope.PeopleFragmentScope
import com.example.messengerapp.presentation.elm.people.PeopleStoreFactory
import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.user.UserDiffUtilCallback
import com.example.messengerapp.presentation.recyclerview.user.UserHolderFactory
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

@Module(includes = [PeopleBindModule::class])
class PeopleModule {

    @PeopleFragmentScope
    @Provides
    fun provideSearchUserSubject(): PublishSubject<String> = PublishSubject.create()

    @PeopleFragmentScope
    @Provides
    fun provideStore(storeFactory: PeopleStoreFactory) = storeFactory.provide()

    @PeopleFragmentScope
    @Provides
    fun provideUserSubject(): PublishSubject<UserUi> = PublishSubject.create()

    @PeopleFragmentScope
    @Provides
    fun provideUserStatusSubject(): PublishSubject<Long> = PublishSubject.create()

    @PeopleFragmentScope
    @Provides
    fun provideAdapter(
        holderFactory: UserHolderFactory,
        diffUtilCallback: UserDiffUtilCallback
    ): AsyncAdapter<ViewTyped> = AsyncAdapter(holderFactory, diffUtilCallback)

    @Provides
    fun provideLinearLayoutManager(context: Context): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
}