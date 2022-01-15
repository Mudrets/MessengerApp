package com.example.messengerapp.presentation.dialog.manager

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.messengerapp.presentation.dialog.ErrorDialog
import com.example.messengerapp.presentation.dialog.command.DialogCommand

abstract class DialogManager<T : Enum<T>>(
    private val fragmentManager: FragmentManager,
    private val tag: String = ""
) {
    private val listenersMap: MutableMap<T, (Bundle) -> Unit> = mutableMapOf()

    var lifecycleOwner: LifecycleOwner? = null

    abstract fun throwableToMessage(error: Throwable, vararg args: String): Pair<String, String>

    abstract fun showDialog(action: DialogCommand<T>)

    abstract fun actionTypeToResKey(actionType: T): String

    open fun addListener(type: T, listener: (Bundle) -> Unit) {
        listenersMap[type] = listener
    }

    open fun showError(title: String, content: String) {
        ErrorDialog.newInstance(title, content)
            .show(fragmentManager, "$tag.$ERROR_DIALOG_TAG")
    }

    fun showError(error: Throwable, vararg args: String) {
        val (title, content) = throwableToMessage(error, *args)
        showError(title, content)
    }

    protected fun showDialog(dialog: DialogFragment, actionType: T) {
        if (lifecycleOwner?.lifecycle?.currentState == Lifecycle.State.RESUMED)
            dialog.show(fragmentManager, "$tag.${actionType.name}")
    }

    fun setListeners() {
        listenersMap.forEach { (actionType, listener) ->
            val resKey = actionTypeToResKey(actionType)
            fragmentManager.setFragmentResultListener(resKey, lifecycleOwner!!) { _, bundle ->
                listener(bundle)
            }
        }
    }

    companion object {
        const val ACTION_DATA_KEY = "ActionDataKey"
        private const val ERROR_DIALOG_TAG = "ErrorDialog"
    }
}