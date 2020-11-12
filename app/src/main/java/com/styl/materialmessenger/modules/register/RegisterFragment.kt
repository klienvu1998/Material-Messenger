package com.styl.materialmessenger.modules.register

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.BaseFragment
import com.styl.materialmessenger.modules.dialog.MessageDialogFragment
import com.styl.materialmessenger.modules.loading.LoadingFragment
import com.styl.materialmessenger.modules.login.LoginActivity
import com.styl.materialmessenger.utils.GeneralUtils
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: BaseFragment(), View.OnClickListener {

    companion object {
        val TAG = RegisterFragment::class.java.simpleName
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()
        v?.findViewById<TextView>(R.id.btnRegister_signUp)?.setOnClickListener(this)
        v?.findViewById<ConstraintLayout>(R.id.registerParentView)?.setOnClickListener(this)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_register
    }

    private fun registerAccount(userName: String, email: String, password: String, phoneNumber: String) {
        firebaseAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth?.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { it0 ->
                            if (it0.isSuccessful) {
                                val firebaseUser = firebaseAuth?.currentUser
                                val userId = firebaseUser?.uid
                                databaseReference = userId?.let { FirebaseDatabase.getInstance().getReference("Users").child(it) }

                                val hashMap: HashMap<String,String> = HashMap()
                                userId?.let { hashMap.put("id", it) }
                                hashMap["username"] = userName
                                hashMap["phonenumber"] = phoneNumber

                                databaseReference?.setValue(hashMap)?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        dismissLoading()
                                        val dialogFragment = MessageDialogFragment.newInstance("Verification Email", "An email is send to your email for verification")
                                        (context as LoginActivity).supportFragmentManager.beginTransaction()
                                            .add(dialogFragment as Fragment, LoadingFragment::class.java.simpleName)
                                            .commitAllowingStateLoss()
                                        dialogFragment.listener = object: MessageDialogFragment.MessageDialogListener {
                                            override fun onNegativeClickListener(dialogFragment: DialogFragment) {
                                            }

                                            override fun onPositiveClickListener(dialogFragment: DialogFragment) {
                                            }

                                            override fun onNeutralClickListener(dialogFragment: DialogFragment) {
                                                (context as LoginActivity).supportFragmentManager.popBackStack()
                                            }
                                        }
                                    } else {
                                        dismissLoading()
                                        showLog(TAG, it.exception?.message.toString())
                                        Toast.makeText(context, "Register fail !!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Register fail !!", Toast.LENGTH_SHORT).show()
                                dismissLoading()
                            }
                        }

                } else {
                    Toast.makeText(context, "Register fail !!", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
            }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnRegister_signUp -> {
                val userName = edtUsername_signUp.text.toString()
                val email = edtEmail_signUp.text.toString()
                val password = edtPassword_signUp.text.toString()
                val phoneNumber = edtPhoneNumber_signUp.text.toString()
                if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                    showToast("All fields are required !!")
                } else if (password.length < 6) {
                    showToast("Password must be at least 6 characters !!")
                } else {
                    showLoading()
                    registerAccount(userName, email, password, phoneNumber)
                }
            }

            R.id.registerParentView -> {
                GeneralUtils.hideKeyboard(context, v)
            }
        }
    }
}