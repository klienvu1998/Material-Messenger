package com.styl.materialmessenger.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.styl.materialmessenger.modules.loading.LoadingFragment

open class BaseActivity: AppCompatActivity() {
    private var loadingFragment: LoadingFragment? = null

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var databaseReference: DatabaseReference? = null
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var storageReference: StorageReference? = null

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

    fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}