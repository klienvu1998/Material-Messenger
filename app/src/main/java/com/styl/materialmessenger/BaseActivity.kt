package com.styl.materialmessenger

import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.styl.materialmessenger.modules.loading.LoadingFragment

open class BaseActivity: AppCompatActivity() {
    private var loadingFragment: LoadingFragment? = null

    fun showLoading() {
        if (loadingFragment == null) {
            loadingFragment =
                LoadingFragment()
            supportFragmentManager?.beginTransaction()
                ?.add(loadingFragment as Fragment, LoadingFragment::class.java.simpleName)
        }
    }

    fun dismissLoading() {
        loadingFragment?.dismissAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        dismissLoading()
    }

    override fun onStart() {
        super.onStart()
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}