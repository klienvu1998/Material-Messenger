package com.styl.materialmessenger.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class GeneralUtils {
    companion object {
        fun hideKeyboard(context: Context?, v: View? = null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val view = v ?: ((context as? Activity)?.currentFocus)
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}