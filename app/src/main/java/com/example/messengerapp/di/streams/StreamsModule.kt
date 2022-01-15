package com.example.messengerapp.di.streams

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.di.annotation.qualifier.DividerItemDecorator
import com.example.messengerapp.di.annotation.scope.StreamsFragmentScope
import com.example.messengerapp.presentation.elm.stream.StreamStoreFactory
import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.ViewTyped
import com.example.messengerapp.presentation.recyclerview.stream.StreamDiffUtilCallback
import com.example.messengerapp.presentation.recyclerview.stream.StreamHolderFactory
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

@Module
class StreamsModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideLinearLayoutManager(context: Context): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    @StreamsFragmentScope
    @Provides
    fun provideOpenChatSubject(): PublishSubject<Triple<String, Long, String>> =
        PublishSubject.create()

    @Provides
    fun provideItemDecoration(context: Context): RecyclerView.ItemDecoration {
        val topicDividerItemDecoration =
            DividerItemDecoration(context, RecyclerView.VERTICAL)
        topicDividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.topic_divider_drawable,
                null
            )!!
        )
        return topicDividerItemDecoration
    }

    @DividerItemDecorator
    @Provides
    fun provideDividerItemDecoration(context: Context): RecyclerView.ItemDecoration {
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.streams_divider_drawable,
                null
            )!!
        )
        return dividerItemDecoration
    }

    @Provides
    fun provideAdapter(
        holderFactory: StreamHolderFactory,
        diffUtilCallback: StreamDiffUtilCallback
    ): AsyncAdapter<ViewTyped> = AsyncAdapter(holderFactory, diffUtilCallback)

    @StreamsFragmentScope
    @Provides
    fun provideStore(
        storeFactory: StreamStoreFactory,
        isSubscribed: Boolean
    ) = storeFactory.provide(isSubscribed)
}