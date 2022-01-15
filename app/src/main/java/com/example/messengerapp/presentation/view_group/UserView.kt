package com.example.messengerapp.presentation.view_group

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.*
import coil.load
import coil.transform.CircleCropTransformation
import com.example.messengerapp.R
import com.example.messengerapp.domain.entity.user.UserStatus
import com.example.messengerapp.presentation.recyclerview.user.UserUi
import com.example.messengerapp.util.ext.fastLazy

class UserView @JvmOverloads constructor(
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

    private var _email: String = ""
        set(value) {
            findViewById<TextView>(R.id.userEmailTextView).text = value
            field = value
        }

    private var _userName: String = ""
        set(value) {
            findViewById<TextView>(R.id.userNameTextView).text = value
            field = value
        }

    private var _status: UserStatus = UserStatus.OFFLINE
        set(value) {
            val statusView = findViewById<View>(R.id.userOnlineStatus)
            statusView.isVisible =
                value == UserStatus.ACTIVE || value == UserStatus.IDLE
            when (value) {
                UserStatus.ACTIVE -> statusView.backgroundTintList =
                    context.getColorStateList(R.color.active_status_color)
                UserStatus.IDLE -> statusView.backgroundTintList =
                    context.getColorStateList(R.color.idle_status_color)
                UserStatus.OFFLINE -> {
                }
            }
            field = value
        }

    private var _imageUrl: String? = null
        set(url) {
            val avatar = findViewById<ImageView>(R.id.userImage)
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

    init {
        inflate(context, R.layout.user_view_group, this)
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.UserView,
            defStyleAttr,
            defStyleRes
        )

        _userName = typedArray.getString(R.styleable.UserView_user_name).orEmpty()
        _email = typedArray.getString(R.styleable.UserView_user_email).orEmpty()
        when (typedArray.getInt(R.styleable.UserView_status, 2)) {
            0 -> _status = UserStatus.ACTIVE
            1 -> _status = UserStatus.IDLE
            2 -> _status = UserStatus.OFFLINE
        }

        typedArray.recycle()
    }

    var userName: String
        get() = _userName
        set(value) {
            _userName = value
            requestLayout()
        }

    var email: String
        get() = _email
        set(value) {
            _email = value
            requestLayout()
        }

    var status: UserStatus
        get() = _status
        set(value) {
            _status = value
            requestLayout()
        }

    var user: UserUi
        get() = UserUi(
            name = userName,
            email = email,
            status = status,
            avatarUrl = null,
            uid = "fromUserView"
        )
        set(user) {
            _userName = user.name
            _email = user.email
            _imageUrl = user.avatarUrl
            _status = user.status
            requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val image = getChildAt(0)
        val usernameTextView = getChildAt(1)
        val emailTextView = getChildAt(2)
        val onlineStatusView = getChildAt(3)

        onlineStatusView.measureWithMargins(widthMeasureSpec, 0, heightMeasureSpec, 0)

        val (imgHorizontalMargins, imgVerticalMargins) =
            image.measureWithMargins(
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                0
            )
        val imageWidth = image.measuredWidth + imgHorizontalMargins
        val imageHeight = image.measuredHeight + imgVerticalMargins

        val (usernameTextViewHorizontalMargins, usernameTextViewVerticalMargins) =
            usernameTextView.measureWithMargins(
                widthMeasureSpec,
                imageWidth,
                heightMeasureSpec,
                0
            )
        val usernameTextViewWidth =
            usernameTextView.measuredWidth + usernameTextViewHorizontalMargins
        val usernameTextViewHeight =
            usernameTextView.measuredHeight + usernameTextViewVerticalMargins

        val (emailTextViewHorizontalMargins, emailTextViewVerticalMargins) =
            emailTextView.measureWithMargins(
                widthMeasureSpec,
                imageWidth,
                heightMeasureSpec,
                usernameTextViewHeight
            )
        val emailTextViewWidth = emailTextView.measuredWidth + emailTextViewHorizontalMargins
        val emailTextViewHeight = emailTextView.measuredHeight + emailTextViewVerticalMargins

        val totalWidth = imageWidth + maxOf(
            usernameTextViewWidth,
            emailTextViewWidth
        ) + paddingLeft + paddingRight
        val totalHeight = maxOf(
            imageHeight,
            usernameTextViewHeight + emailTextViewHeight
        ) + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val image = getChildAt(0)
        val usernameTextView = getChildAt(1)
        val emailTextView = getChildAt(2)
        val onlineStatusView = getChildAt(3)

        image.layout(
            paddingLeft + image.marginLeft,
            paddingTop + image.marginTop,
            paddingLeft + image.marginLeft + image.measuredWidth,
            paddingTop + image.marginTop + image.measuredHeight
        )

        val imageWidth = image.width + image.marginRight + image.marginLeft

        usernameTextView.layout(
            paddingLeft + imageWidth + usernameTextView.marginLeft,
            paddingTop + usernameTextView.marginTop,
            paddingLeft + imageWidth + usernameTextView.marginLeft + usernameTextView.measuredWidth,
            paddingTop + usernameTextView.marginTop + usernameTextView.measuredHeight,
        )

        val usernameTextViewHeight =
            usernameTextView.height + usernameTextView.marginTop + usernameTextView.marginBottom

        emailTextView.layout(
            paddingLeft + imageWidth + usernameTextView.marginLeft,
            paddingTop + usernameTextViewHeight + emailTextView.marginTop,
            paddingLeft + imageWidth + emailTextView.marginLeft + emailTextView.measuredWidth,
            paddingTop + usernameTextViewHeight + emailTextView.marginTop + emailTextView.measuredHeight
        )

        onlineStatusView.layout(
            image.right - onlineStatusView.measuredWidth,
            image.bottom - onlineStatusView.measuredHeight,
            image.right,
            image.bottom
        )
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

}