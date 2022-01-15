package com.example.messengerapp.presentation.view_group.message

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import coil.load
import coil.transform.CircleCropTransformation
import com.example.messengerapp.R
import com.example.messengerapp.domain.entity.chat.MessageType
import com.example.messengerapp.domain.entity.chat.SendingStatus
import com.example.messengerapp.presentation.recyclerview.chat.MessageUi
import com.example.messengerapp.presentation.view.EmojiView
import com.example.messengerapp.presentation.view_group.FlexBox
import com.example.messengerapp.util.ext.fastLazy

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val defaultImage by fastLazy {
        val avatar = RoundedBitmapDrawableFactory.create(
            resources, AppCompatResources.getDrawable(
                context,
                R.mipmap.ic_launcher
            )?.toBitmap()
        )
        avatar.isCircular = true
        avatar
    }

    private val emojiList: EmojiList

    private val endMessagePadding = 150

    private var _marginBetweenEmoji: Float = 0f
        set(value) {
            findViewById<FlexBox>(R.id.emojiFlexBox).elemMargin = value
            field = value
        }

    private var _marginBetweenEmojiRows: Float = 0f
        set(value) {
            findViewById<FlexBox>(R.id.emojiFlexBox).rowMargin = value
            field = value
        }

    private var _messageType: MessageType = MessageType.INCOMING_MESSAGE
        set(value) {
            val flexBox = findViewById<FlexBox>(R.id.emojiFlexBox)
            flexBox.alignment = value.ordinal
            when (value) {
                MessageType.INCOMING_MESSAGE -> {
                    findViewById<TextView>(R.id.senderName).visibility = VISIBLE
                    _messageBackground =
                        AppCompatResources.getDrawable(context, R.drawable.bg_message)
                }
                MessageType.OUTGOING_MESSAGE -> {
                    findViewById<TextView>(R.id.senderName).visibility = GONE
                    _messageBackground =
                        AppCompatResources.getDrawable(context, R.drawable.bg_my_message)
                }
            }
            field = value
        }

    private var _addEmojiButtonGone: Boolean? = null

    private var _messageBackground: Drawable? = null
        set(value) {
            findViewById<LinearLayout>(R.id.messageBox).background = value
            field = value
        }

    private var _messageTextColor: Int = -1
        set(value) {
            if (value >= 0)
                findViewById<TextView>(R.id.messageText).setTextColor(value)
            field = value
        }

    private var _senderNameColor: Int = -1
        set(value) {
            if (value >= 0)
                findViewById<TextView>(R.id.senderName).setTextColor(value)
            field = value
        }


    private var _imageUrl: String? = null
        set(url) {
            val avatar = findViewById<ImageView>(R.id.avatar)
            if (url != null) {
                avatar.load(url) {
                    crossfade(true)
                    error(defaultImage)
                    placeholder(defaultImage)
                    transformations(
                        CircleCropTransformation()
                    )
                }
            } else {
                avatar.setImageDrawable(defaultImage)
            }
            field = url
        }

    private var _isProgressBarVisible: Boolean = false
        set(value) {
            val progressBar = findViewById<ProgressBar>(R.id.messageProgressBar)
            progressBar.isVisible = value
            field = value
        }

    private var _isWarningVisible: Boolean = false
        set(value) {
            findViewById<ImageView>(R.id.messageWarningIcon).visibility =
                if (value) View.VISIBLE else View.GONE
            field = value
        }

    var marginBetweenEmoji: Float
        get() = _marginBetweenEmoji
        set(value) {
            _marginBetweenEmoji = value
            requestLayout()
        }

    var marginBetweenEmojiRows: Float
        get() = _marginBetweenEmojiRows
        set(value) {
            _marginBetweenEmojiRows = value
            requestLayout()
        }

    var messageType: MessageType
        get() = _messageType
        set(value) {
            _messageType = value
            requestLayout()
        }

    var addEmojiButtonGone: Boolean
        get() =
            _addEmojiButtonGone ?: emojiList.isEmpty()
        set(value) {
            _addEmojiButtonGone = value
            requestLayout()
        }

    var messageBackground: Drawable?
        get() = _messageBackground
        set(value) {
            _messageBackground = value
            invalidate()
        }

    var messageTextColor: Int
        get() = _messageTextColor
        set(value) {
            _messageTextColor = value
            invalidate()
        }

    var senderNameColor: Int
        get() = _senderNameColor
        set(value) {
            _senderNameColor = value
            invalidate()
        }

    var isProgressBarVisible: Boolean
        get() = _isProgressBarVisible
        set(value) {
            val oldValue = _isProgressBarVisible
            _isProgressBarVisible = value
            if (oldValue != value) {
                requestLayout()
            }
        }

    var isWarningVisible: Boolean
        get() = _isWarningVisible
        set(value) {
            val oldValue = _isWarningVisible
            _isWarningVisible = value
            if (oldValue != value) {
                requestLayout()
            }
        }

    private var _messageText: String = ""
        set(value) {
            findViewById<TextView>(R.id.messageText).text = value
            field = value
        }

    private var senderName: String = ""
        set(value) {
            findViewById<TextView>(R.id.senderName).apply {
                if (value.isBlank()) {
                    visibility = GONE
                } else {
                    text = value
                }
            }

            field = value
        }

    var messageText: String
        get() = _messageText
        set(newText) {
            _messageText = newText
            requestLayout()
        }

    var emojiOnClickListener: (EmojiView) -> Unit = {}

    var addEmojiButtonOnClickListener: () -> Unit = {}

    var messageOnLongClickListener: () -> Unit = {}

    var message: MessageUi? = null
        set(value) {
            if (value != null) {
                _imageUrl = value.avatarUrl
                _messageText = value.messageText
                senderName = value.senderName
                _messageType = value.messageType
                emojiList.set(value.emojiList)
                _isProgressBarVisible = value.sendingStatus == SendingStatus.IS_SENDING
                _isWarningVisible = value.sendingStatus == SendingStatus.ERROR
                _addEmojiButtonGone =
                    if (value.messageType == MessageType.OUTGOING_MESSAGE) true else null
            }
            field = value
            val addEmojiButton = findViewById<ImageView>(R.id.addEmojiButton)
            addEmojiButton.isVisible = !addEmojiButtonGone
            requestLayout()
        }

    init {
        inflate(context, R.layout.message_view_group_layout, this)
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MessageViewGroup,
            defStyleAttr,
            defStyleRes
        )


        _marginBetweenEmoji =
            typedArray.getDimension(R.styleable.MessageViewGroup_margin_between_emoji, 20f)
        _marginBetweenEmojiRows =
            typedArray.getDimension(R.styleable.MessageViewGroup_margin_between_emoji_rows, 20f)
        _messageBackground = typedArray.getDrawable(R.styleable.MessageViewGroup_message_background)
        _senderNameColor = typedArray.getColor(
            R.styleable.MessageViewGroup_sender_name_color,
            resources.getColor(R.color.aquamarine, null)
        )

        val flexBox = findViewById<FlexBox>(R.id.emojiFlexBox)
        emojiList = EmojiList(flexBox, context)
        flexBox.setRowHeightToLastImage = true

        _messageTextColor = typedArray.getColor(R.styleable.MessageViewGroup_message_text_color, -1)
        _isProgressBarVisible = isProgressBarVisible

        typedArray.recycle()

        setEmojiOnClickListener()
        setAddButtonOnClickListener()
        setMessageLongClickListener()
    }

    private fun setEmojiOnClickListener() {
        val addEmojiButton = findViewById<ImageView>(R.id.addEmojiButton)
        emojiList.setEmojiOnClickListener { emojiView ->
            addEmojiButton.visibility = if (addEmojiButtonGone) GONE else VISIBLE
            emojiOnClickListener(emojiView)
        }
    }

    private fun setAddButtonOnClickListener() {
        findViewById<View>(R.id.addEmojiButton).setOnClickListener {
            addEmojiButtonOnClickListener()
        }
    }

    private fun setMessageLongClickListener() {
        val messageLayout = findViewById<LinearLayout>(R.id.messageBox)
        messageLayout.setOnLongClickListener {
            if (!isWarningVisible)
                messageOnLongClickListener()
            !isWarningVisible
        }
    }

    private fun View.measureWithMargins(
        widthMeasureSpec: Int,
        widthUsed: Int,
        heightMeasureSpec: Int,
        heightUsed: Int
    ): Pair<Int, Int> {
        measureChildWithMargins(this, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
        val margins = (this.layoutParams as MarginLayoutParams)
        return Pair(
            margins.leftMargin + margins.rightMargin,
            margins.topMargin + margins.bottomMargin
        )
    }

    private fun getTotalWidthAndHeight(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Pair<Int, Int> {
        val img = getChildAt(0)
        val message = getChildAt(1)
        val flexBox = getChildAt(2)
        val progressBar = getChildAt(3)
        val warningIcon = getChildAt(4)

        val (imgHorizontalMargins, imgVerticalMargins) =
            img.measureWithMargins(
                widthMeasureSpec,
                endMessagePadding,
                heightMeasureSpec,
                0
            )
        val imageWidth = img.measuredWidth + imgHorizontalMargins
        val imageHeight = img.measuredHeight + imgVerticalMargins

        warningIcon.measureWithMargins(
            widthMeasureSpec,
            imageWidth,
            heightMeasureSpec,
            0
        )

        val (progressBarHorizontalMargins, progressBarVerticalMargins) =
            progressBar.measureWithMargins(
                widthMeasureSpec,
                imageWidth,
                heightMeasureSpec,
                0
            )
        val progressBarWidth = progressBar.measuredWidth + progressBarHorizontalMargins
        val progressBarHeight = progressBar.measuredHeight + progressBarVerticalMargins

        val (messageHorizontalMargins, messageVerticalMargins) =
            message.measureWithMargins(
                widthMeasureSpec,
                endMessagePadding + imageWidth + progressBarWidth,
                heightMeasureSpec,
                0
            )
        val messageWidth = message.measuredWidth + messageHorizontalMargins
        val messageHeight = message.measuredHeight + messageVerticalMargins

        val (flexBoxHorizontalMargins, flexBoxVerticalMargins) =
            flexBox.measureWithMargins(
                widthMeasureSpec,
                imageWidth,
                heightMeasureSpec,
                0
            )
        val flexBoxWidth = flexBox.measuredWidth + flexBoxHorizontalMargins
        val flexBoxHeight = flexBox.measuredHeight + flexBoxVerticalMargins


        return Pair(
            paddingLeft + imageWidth + maxOf(
                flexBoxWidth,
                messageWidth + progressBarWidth
            ) + paddingRight,
            paddingTop + maxOf(
                flexBoxHeight + messageHeight,
                progressBarHeight + flexBoxHeight,
                imageHeight
            ) + paddingBottom
        )
    }

    private fun onLayoutAlignRight() {
        val img = getChildAt(0)
        val message = getChildAt(1)
        val flexBox = getChildAt(2)
        val loadOrErrorIcon = if (isWarningVisible)
            getChildAt(4)
        else
            getChildAt(3)

        img.layout(0, 0, 0, 0)

        message.layout(
            width - paddingRight - message.marginRight - message.measuredWidth,
            paddingTop + message.marginTop,
            width - paddingRight - message.marginRight,
            paddingTop + message.marginTop + message.measuredHeight
        )

        flexBox.layout(
            message.right - flexBox.marginRight - flexBox.measuredWidth,
            message.bottom + flexBox.marginTop,
            message.right - flexBox.marginRight,
            message.bottom + flexBox.marginTop + flexBox.measuredHeight
        )

        if (isWarningVisible || isProgressBarVisible) {
            loadOrErrorIcon.layout(
                message.left - loadOrErrorIcon.marginRight - loadOrErrorIcon.measuredWidth,
                message.bottom - loadOrErrorIcon.measuredHeight,
                message.left - loadOrErrorIcon.marginRight,
                message.bottom
            )
        } else {
            loadOrErrorIcon.layout(0, 0, 0, 0)
        }

    }

    private fun onLayoutAlignLeft() {
        val img = getChildAt(0)
        val message = getChildAt(1)
        val flexBox = getChildAt(2)
        val loadOrErrorIcon = if (isWarningVisible)
            getChildAt(4)
        else
            getChildAt(3)

        img.layout(
            paddingLeft + img.marginLeft,
            paddingTop + img.marginTop,
            paddingLeft + img.marginLeft + img.measuredWidth,
            paddingTop + img.marginTop + img.measuredHeight
        )

        message.layout(
            img.right + img.marginRight + message.marginLeft,
            paddingTop + message.marginTop,
            img.right + img.marginRight + message.marginLeft + message.measuredWidth,
            paddingTop + message.marginTop + message.measuredHeight
        )

        flexBox.layout(
            message.left + flexBox.marginLeft,
            message.bottom + flexBox.marginTop,
            message.left + flexBox.marginLeft + flexBox.measuredWidth,
            message.bottom + flexBox.marginTop + flexBox.measuredHeight
        )

        if (isWarningVisible || isProgressBarVisible) {
            loadOrErrorIcon.layout(
                message.right + loadOrErrorIcon.marginLeft,
                message.bottom - loadOrErrorIcon.measuredHeight,
                message.right + loadOrErrorIcon.marginLeft + loadOrErrorIcon.measuredWidth,
                message.bottom
            )
        } else {
            loadOrErrorIcon.layout(0, 0, 0, 0)
        }
    }

    fun setNewEmojiList(newEmojiList: List<EmojiUi>) {
        emojiList.set(newEmojiList)
        val addEmojiButton = findViewById<ImageView>(R.id.addEmojiButton)
        addEmojiButton.isVisible = !addEmojiButtonGone
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 5) {
            "Incorrect number of children. MessageViewGroup must have only 5 children"
        }

        val (totalWidth, totalHeight) = getTotalWidthAndHeight(widthMeasureSpec, heightMeasureSpec)

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        when (messageType) {
            MessageType.INCOMING_MESSAGE -> onLayoutAlignLeft()
            MessageType.OUTGOING_MESSAGE -> onLayoutAlignRight()
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }
}