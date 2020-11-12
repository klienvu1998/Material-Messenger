package com.styl.materialmessenger.modules.login.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.styl.materialmessenger.modules.home.view.HomeActivity
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.BaseFragment
import com.styl.materialmessenger.modules.login.LoginContract
import com.styl.materialmessenger.modules.login.presenter.LoginPresenter
import com.styl.materialmessenger.utils.GeneralUtils
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseFragment(), View.OnClickListener, LoginContract.View {

    private var presenter: LoginPresenter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        presenter = LoginPresenter(this, context)
        val loginLabelAnim = AnimationUtils.loadAnimation(context,
            R.anim.top_to_bottom
        )
        val edtAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val btnLoginAnim = AnimationUtils.loadAnimation(context,
            R.anim.bottom_to_top
        )

        v?.findViewById<TextView>(R.id.loginLabel)?.startAnimation(loginLabelAnim)
        v?.findViewById<TextView>(R.id.signupLabel)?.startAnimation(btnLoginAnim)
        v?.findViewById<TextView>(R.id.btnSignUp)?.startAnimation(btnLoginAnim)
        v?.findViewById<EditText>(R.id.edtUsername_signIn)?.startAnimation(edtAnim)
        v?.findViewById<EditText>(R.id.edtPassword_signIn)?.startAnimation(edtAnim)
        v?.findViewById<TextView>(R.id.btnLogin)?.startAnimation(btnLoginAnim)

        v?.findViewById<TextView>(R.id.btnLogin)?.setOnClickListener(this)
        v?.findViewById<TextView>(R.id.btnSignUp)?.setOnClickListener(this)
        v?.findViewById<ConstraintLayout>(R.id.loginParentView)?.setOnClickListener(this)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_login
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnLogin -> {
                val email = edtUsername_signIn.text.toString()
                val password = edtPassword_signIn.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    showToast("All fields are required !!")
                } else {
                    signIn(email, password)
                }
            }

            R.id.btnSignUp -> {
                presenter?.navigateToRegister()
            }

            R.id.loginParentView -> {
                GeneralUtils.hideKeyboard(context, v)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        showLoading()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (firebaseAuth.currentUser?.isEmailVerified == true) {
                        dismissLoading()
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        activity?.finish()
                    } else {
                        showToast("Your email not verification !!")
                    }
                } else {
                    showToast("Wrong email or password !!")
                }
            }
    }
}