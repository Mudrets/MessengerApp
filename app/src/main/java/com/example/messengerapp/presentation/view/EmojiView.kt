package com.example.messengerapp.presentation.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.messengerapp.R
import com.example.messengerapp.presentation.view_group.message.EmojiUi

class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var _num: Int = 0
    private var _emojiCode: String = "\uD83D\uDE03"
    private var _sizeOfEmoji: Float
    private var _showNumber: Boolean
    private var _emojiName: String = ""

    var emojiName
        get() = _emojiName
        set(value) {
            _emojiName = value
        }

    var num
        get() = _num
        set(value) {
            _num = value
            requestLayout()
        }

    var emojiCode
        get() = _emojiCode
        set(value) {
            _emojiCode = value
            requestLayout()
        }

    var sizeOfEmoji: Float
        get() = _sizeOfEmoji
        set(value) {
            _sizeOfEmoji = value
            requestLayout()
        }

    var emoji: EmojiUi
        get() = EmojiUi(emojiCode, _emojiName, num, isSelected)
        set(value) {
            _num = value.count
            _emojiCode = value.emojiCode
            isSelected = value.isSelected
            _emojiName = value.emojiName
            requestLayout()
        }

    var showNumber: Boolean
        get() = _showNumber
        set(value) {
            _showNumber = value
            requestLayout()
        }

    private var numberColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    private val textBounds = Rect()
    private val textCoordinate = PointF()

    private val text: String
        get() = if (showNumber) "$_emojiCode $_num" else _emojiCode

    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private val tempFontMetrics = Paint.FontMetrics()

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiView,
            defStyleAttr,
            defStyleRes
        )

        _num = typedArray.getInt(R.styleable.EmojiView_number, 0)
        _emojiCode = typedArray.getString(R.styleable.EmojiView_emoji) ?: _emojiCode
        _sizeOfEmoji = typedArray.getDimension(R.styleable.EmojiView_size, 70f)
        _showNumber = typedArray.getBoolean(R.styleable.EmojiView_showNumber, true)
        numberColor = typedArray.getColor(R.styleable.EmojiView_number_color, Color.BLACK)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        emojiPaint.textSize = _sizeOfEmoji
        emojiPaint.color = numberColor
        emojiPaint.getTextBounds(text, 0, text.length, textBounds)

        val textWidth = textBounds.width()
        val textHeight = textBounds.height()

        val totalWidth = textWidth + paddingRight + paddingLeft
        val totalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        emojiPaint.getFontMetrics(tempFontMetrics)
        textCoordinate.x = w / 2f
        textCoordinate.y = h / 2f + textBounds.height() / 2 - tempFontMetrics.descent
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, emojiPaint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState =
            super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }

    companion object {
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }
}