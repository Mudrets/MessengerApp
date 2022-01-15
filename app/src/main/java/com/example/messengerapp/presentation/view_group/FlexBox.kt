package com.example.messengerapp.presentation.view_group

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.*
import com.example.messengerapp.R

class FlexBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    internal var setRowHeightToLastImage = false

    private var _elemMargin: Float
    private var _rowMargin: Float
    private var _alignment: Int

    private var currentStart = 0
    private var currentTop = 0
    private var rowHeight = 0

    var elemMargin
        get() = _elemMargin
        set(value) {
            _elemMargin = value
            requestLayout()
        }

    var rowMargin
        get() = _rowMargin
        set(value) {
            _rowMargin = value
            requestLayout()
        }

    var alignment
        get() = _alignment
        set(value) {
            _alignment = value
            requestLayout()
        }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.FlexBox,
            defStyleAttr,
            defStyleRes
        )

        _elemMargin = typedArray.getDimension(R.styleable.FlexBox_margin_between_elements, 0f)
        _rowMargin = typedArray.getDimension(R.styleable.FlexBox_margin_between_rows, 0f)
        _alignment = typedArray.getInt(R.styleable.FlexBox_elements_alignment, 0)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (totalWidth, totalHeight) = findTotalWidthAndHeight(widthMeasureSpec, heightMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        currentTop = paddingTop
        rowHeight = 0

        currentStart = if (alignment == 0)
            paddingStart
        else
            width - paddingEnd

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.isVisible) {
                if (alignment == 0)
                    onLayoutAlignmentLeft(child, i)
                else
                    onLayoutAlignmentRight(child, i)
                rowHeight = maxOf(rowHeight, child.height + child.marginTop + child.marginBottom)
            }
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

    private fun onLayoutAlignmentLeft(child: View, index: Int) {
        val oldRowHeight = rowHeight
        if (currentStart + child.measuredWidth + child.marginLeft + child.marginRight > width - paddingEnd) {
            currentStart = paddingStart
            currentTop += rowHeight + rowMargin.toInt()
            rowHeight = 0
        }
        val left = currentStart + child.marginLeft
        val right = currentStart + child.marginLeft + child.measuredWidth + child.marginRight
        val top = currentTop + child.marginTop
        val bottom = if (index == childCount - 1 && child is ImageView)
            currentTop + oldRowHeight - child.marginBottom
        else
            currentTop + child.marginTop + child.measuredHeight
        child.layout(left, top, right, bottom)
        currentStart += child.marginLeft + child.width + child.marginRight + elemMargin.toInt()
    }

    private fun onLayoutAlignmentRight(child: View, index: Int) {
        val oldRowHeight = rowHeight
        if (currentStart - child.measuredWidth - child.marginRight - child.marginRight < paddingStart) {
            currentStart = width - paddingEnd
            currentTop += rowHeight + rowMargin.toInt()
            rowHeight = 0
        }
        val left = currentStart - child.marginRight - child.measuredWidth
        val right = currentStart - child.marginRight
        val top = currentTop + child.marginTop
        val bottom = if (index == childCount - 1 && child is ImageView)
            currentTop + oldRowHeight - child.marginBottom
        else
            currentTop + child.marginTop + child.measuredHeight
        child.layout(left, top, right, bottom)
        currentStart -= child.marginLeft + child.width + child.marginRight + elemMargin.toInt()
    }

    private fun View.measureWithMargins(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Pair<Int, Int> {
        measureChildWithMargins(this, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val margins = (this.layoutParams as MarginLayoutParams)
        return Pair(
            margins.leftMargin + margins.rightMargin,
            margins.topMargin + margins.bottomMargin
        )
    }

    private fun findTotalWidthAndHeight(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Pair<Int, Int> {
        var rowWidth = 0
        var rowHeight = 0
        var rowCounter = 0
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureMode = MeasureSpec.getMode(widthMeasureSpec)

        var totalWidth = 0
        var totalHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.isVisible) {
                val (horizontalMargins, verticalMargins) =
                    child.measureWithMargins(widthMeasureSpec, heightMeasureSpec)

                val childWidthWithMargin: Int = if (i == 0)
                    child.measuredWidth + horizontalMargins
                else
                    child.measuredWidth + elemMargin.toInt() + horizontalMargins

                if (measureMode != MeasureSpec.UNSPECIFIED) {
                    if (rowWidth + childWidthWithMargin <= measureWidth - paddingEnd - paddingStart) {
                        rowWidth += childWidthWithMargin
                        rowHeight = maxOf(rowHeight, child.measuredHeight + verticalMargins)
                    } else { // Переход на следующую строку
                        rowCounter++
                        totalWidth = maxOf(totalWidth, rowWidth)
                        totalHeight += rowHeight
                        rowWidth = child.measuredWidth + horizontalMargins
                        rowHeight = child.measuredHeight + verticalMargins
                    }
                } else {
                    totalWidth += childWidthWithMargin + horizontalMargins
                    totalHeight = maxOf(totalHeight, child.measuredHeight + verticalMargins)
                }
            }
        }
        rowCounter++
        totalWidth = maxOf(totalWidth, rowWidth) + paddingEnd + paddingStart
        totalHeight +=
            rowHeight + paddingBottom + paddingTop + (rowCounter - 1) * rowMargin.toInt()

        return Pair(totalWidth, totalHeight)
    }

    fun setChildrenOnClickListener(
        onClickListener: (View) -> Unit
    ) {
        for (child in children) {
            child.setOnClickListener(onClickListener)
        }
    }
}