package com.styl.materialmessenger.modules

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.styl.materialmessenger.modules.dialog.MessageDialogFragment
import com.styl.materialmessenger.modules.loading.LoadingFragment

abstract class BaseDialogFragment : DialogFragment() {

    private var loadingFragment: LoadingFragment? = null
    var dialogFragment: MessageDialogFragment? = null

    fun showLoading() {
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment()
            fragmentManager?.beginTransaction()
                ?.add(loadingFragment as Fragment, LoadingFragment::class.java.simpleName)
                ?.commitAllowingStateLoss()
        }
    }

    fun dismissLoading() {
        if (loadingFragment?.isAdded == true) {
            loadingFragment?.dismissAllowingStateLoss()
            loadingFragment = null
        }
    }

    fun showErrorMessage(messageResId: Int) {
        dismissAllowingStateLoss()

        dialogFragment = MessageDialogFragment.newInstance(0, messageResId)
        fragmentManager?.beginTransaction()
            ?.add(dialogFragment as Fragment, LoadingFragment::class.java.simpleName)
            ?.commitAllowingStateLoss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        getViewResource()?.let { dialog?.setContentView(it) }
        isCancelable = false

        // clear background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initialize(savedInstanceState)

        return dialog!!
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        dismissLoading()
    }

    override fun onStop() {
        super.onStop()
        dismissLoading()
    }

    abstract fun getViewResource(): View?
    abstract fun initialize(savedInstanceState: Bundle?)
}