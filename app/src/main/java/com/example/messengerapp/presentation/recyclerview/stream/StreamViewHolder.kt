package com.example.messengerapp.presentation.recyclerview.stream

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.presentation.recyclerview.base.AsyncAdapter
import com.example.messengerapp.presentation.recyclerview.base.HolderFactory
import com.example.messengerapp.presentation.recyclerview.base.composite.CompositeBaseViewHolder
import com.example.messengerapp.presentation.recyclerview.base.composite.CompositeViewTyped
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.parcelize.Parcelize

@Parcelize
class StreamUi(
    val streamName: String,
    @ColorRes
    val activeArrowColorRes: Int,
    @ColorRes
    val notActiveArrowColorRes: Int,
    @StringRes
    val titleTmpRes: Int,
    val streamId: Long,
    override val items: List<TopicUi> = listOf(),
    override val viewType: Int = R.layout.stream_item,
    override val uid: String = streamId.toString()
) : CompositeViewTyped, Parcelable

class StreamViewHolder(
    override val holderFactory: HolderFactory,
    private val view: View,
    private val topicItemDecoration: RecyclerView.ItemDecoration,
    private val openChatPublisher: PublishSubject<Triple<String, Long, String>>
) : CompositeBaseViewHolder<StreamUi>(view) {

    private val adapter = AsyncAdapter(holderFactory, TopicDiffUtilCallback())
    private val streamNameTextView: TextView = view.findViewById(R.id.streamName)
    private val topicsRecyclerView: RecyclerView = view.findViewById(R.id.streamTopics)
    private val expandingArrow: ImageView = view.findViewById(R.id.expandingImage)
    private var isExpanded = false
    private var items = listOf<TopicUi>()

    override fun bind(item: StreamUi) {
        streamNameTextView.text = view.context.getString(item.titleTmpRes, item.streamName)
        topicsRecyclerView.adapter = adapter
        adapter.items = listOf()
        items = item.items
        topicsRecyclerView.visibility = View.VISIBLE

        expandingArrow.rotation = 0f
        expandingArrow.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                adapter.items = items
                topicsRecyclerView.addItemDecoration(topicItemDecoration)
                expandingArrow.rotation = 180f
                expandingArrow.setColorFilter(
                    ContextCompat.getColor(
                        view.context,
                        item.activeArrowColorRes
                    )
                )
            } else {
                adapter.items = listOf()
                topicsRecyclerView.removeItemDecoration(topicItemDecoration)
                expandingArrow.rotation = 0f
                expandingArrow.setColorFilter(
                    ContextCompat.getColor(
                        view.context,
                        item.notActiveArrowColorRes
                    )
                )
            }
        }

        streamNameTextView.setOnClickListener {
            openChatPublisher.onNext(
                Triple(
                    item.streamName,
                    item.streamId,
                    ""
                )
            )
        }
    }

    override fun bind(item: StreamUi, payload: List<Any>) {
        val changeBundle = payload[0] as? Bundle ?: Bundle()
        if (changeBundle.containsKey("streamName")) {
            streamNameTextView.text = changeBundle.getString("streamName") ?: item.streamName
        }
        if (changeBundle.containsKey("items")) {
            items = changeBundle.getParcelableArrayList("items") ?: item.items
        }
    }
}