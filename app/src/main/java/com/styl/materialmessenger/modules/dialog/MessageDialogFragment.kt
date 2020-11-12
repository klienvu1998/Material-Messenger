package com.styl.materialmessenger.modules.dialog

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_message_dialog.view.*

class MessageDialogFragment : BaseDialogFragment(), View.OnClickListener {

    companion object {

        const val ARG_TITLE = ".args.ARG_TITLE"
        const val ARG_MESSAGE = ".args.ARG_MESSAGE"
        private const val ARG_TITLE_RES_ID = ".args.ARG_TITLE_RES_ID"
        private const val ARG_MESSAGE_RES_ID = ".args.ARG_MESSAGE_RES_ID"
        private const val ARG_ERROR_CODE = ".args.ARG_ERROR_CODE"

        fun newInstance(title: String, message: String): MessageDialogFragment {
            val f = MessageDialogFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            f.arguments = args
            return f
        }

        fun newInstance(@StringRes titleResId: Int, @StringRes messageResId: Int): MessageDialogFragment {
            val f = MessageDialogFragment()
            val args = Bundle()
            args.putInt(ARG_TITLE_RES_ID, titleResId)
            args.putInt(ARG_MESSAGE_RES_ID, messageResId)
            f.arguments = args
            return f
        }

        fun newInstance(errorCode: Int, title: String, message: String): MessageDialogFragment {
            val f = MessageDialogFragment()
            val args = Bundle()
            args.putInt(ARG_ERROR_CODE, errorCode)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            f.arguments = args
            return f
        }
    }

    private var title: String? = null
    private var message: String? = null

    @StringRes
    private var titleResId: Int? = null
    @StringRes
    private var messageResId: Int? = null

    private var errorCode: Int? = null

    private var isMulti = false
    private var negativeMessage: String? = null
    private var positiveMessage: String? = null
    private var v: View? = null

    var listener: MessageDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = arguments?.getString(ARG_TITLE)
        message = arguments?.getString(ARG_MESSAGE)
        titleResId = arguments?.getInt(ARG_TITLE_RES_ID)
        messageResId = arguments?.getInt(ARG_MESSAGE_RES_ID)
        errorCode = arguments?.getInt(ARG_ERROR_CODE)
    }

    override fun getViewResource(): View? {
        v = activity?.layoutInflater?.inflate(R.layout.fragment_message_dialog, null)
        return v
    }

    override fun initialize(savedInstanceState: Bundle?) {
        if (!title.isNullOrEmpty()) {
            v?.txtTitle?.text = title
        } else titleResId?.let {
            if (it > 0) {
                v?.txtTitle?.setText(it)
            }
        }

        if (errorCode != null && errorCode!! > 0) {
            val name = "code_$errorCode"
            messageResId = resources.getIdentifier(name, "string", activity?.packageName)
            if (messageResId != null && messageResId!! > 0) {
                v?.txtMessage?.setText(messageResId!!)
            } else {
                v?.txtMessage?.text = message
            }
        } else if (!message.isNullOrEmpty()) {
            v?.txtMessage?.text = message
        } else messageResId?.let {
            if (it > 0) {
                v?.txtMessage?.setText(it)
            }
        }

        if (isMulti) {
            v?.btnNegative?.visibility = View.VISIBLE
            v?.btnNeutral?.visibility = View.INVISIBLE
            v?.btnPositive?.visibility = View.VISIBLE

            if (!negativeMessage.isNullOrEmpty()) {
                v?.txt_btn_negative?.text = negativeMessage
            }
            if (!positiveMessage.isNullOrEmpty()) {
                v?.txt_btn_positive?.text = positiveMessage
            }
        }

        isCancelable = false

        v?.btnNegative?.setOnClickListener(this)
        v?.btnNeutral?.setOnClickListener(this)
        v?.btnPositive?.setOnClickListener(this)
    }

    fun hideButton() {
        v?.btnNegative?.visibility = View.GONE
        v?.btnPositive?.visibility = View.GONE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnNegative -> {
                dismiss()
                listener?.onNegativeClickListener(this@MessageDialogFragment)
            }
            R.id.btnNeutral -> {
                dismiss()
                listener?.onNeutralClickListener(this@MessageDialogFragment)
            }
            R.id.btnPositive -> {
                listener?.onPositiveClickListener(this@MessageDialogFragment)
            }
        }
    }

    // MARK: Public methods

    fun setMultiChoice(negativeMessage: String?, positiveMessage: String?) {
        this.isMulti = true
        this.negativeMessage = negativeMessage
        this.positiveMessage = positiveMessage
    }

    interface MessageDialogListener {

        fun onNegativeClickListener(dialogFragment: DialogFragment)

        fun onPositiveClickListener(dialogFragment: DialogFragment)

        fun onNeutralClickListener(dialogFragment: DialogFragment)
    }
}