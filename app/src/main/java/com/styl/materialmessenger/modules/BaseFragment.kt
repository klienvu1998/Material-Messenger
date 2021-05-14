package com.styl.materialmessenger.modules

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.styl.materialmessenger.modules.loading.LoadingFragment

abstract class BaseFragment: Fragment() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var databaseReference: DatabaseReference? = null
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    private var loadingFragment: LoadingFragment? = null
    var storageReference: StorageReference? = null

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
            storageReference = FirebaseStorage.getInstance().getReference("uploads")
            initializeView(savedInstanceState)
        }

        return v
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLog(name: String, msg: String) {
        Log.d(name, msg)
    }

    abstract fun initializeView(savedInstanceState: Bundle?)
    abstract fun getLayoutResource(): Int
}