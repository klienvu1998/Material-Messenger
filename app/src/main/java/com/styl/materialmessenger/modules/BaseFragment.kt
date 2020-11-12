package com.styl.materialmessenger.modules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.styl.materialmessenger.modules.loading.LoadingFragment

abstract class BaseFragment: Fragment() {
    private var loadingFragment: LoadingFragment? = null
    var v: View? = null

    override fun onStop() {
        super.onStop()
        dismissLoading()
    }

    fun showLoading() {
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment()
            fragmentManager?.beginTransaction()
                ?.add(loadingFragment as Fragment, LoadingFragment::class.java.simpleName)
                ?.commitAllowingStateLoss()
        }
    }

    fun dismissLoading() {
        loadingFragment?.dismissAllowingStateLoss()
        loadingFragment = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (v == null) {
            v = LayoutInflater.from(activity).inflate(getLayoutResource(), container, false)

            initializeView(savedInstanceState)
        }

        return v
    }

    abstract fun initializeView(savedInstanceState: Bundle?)
    abstract fun getLayoutResource(): Int
}