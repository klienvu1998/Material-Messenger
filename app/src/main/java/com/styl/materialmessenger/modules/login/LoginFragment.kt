package com.styl.materialmessenger.modules.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import com.styl.materialmessenger.modules.home.view.HomeActivity
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.BaseFragment

class LoginFragment: BaseFragment(), View.OnClickListener {
    override fun initializeView(savedInstanceState: Bundle?) {
        val loginLabelAnim = AnimationUtils.loadAnimation(context,
            R.anim.top_to_bottom
        )
        val edtAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val btnLoginAnim = AnimationUtils.loadAnimation(context,
            R.anim.bottom_to_top
        )

        v?.findViewById<TextView>(R.id.loginLabel)?.startAnimation(loginLabelAnim)
        v?.findViewById<EditText>(R.id.edtUsername)?.startAnimation(edtAnim)
        v?.findViewById<EditText>(R.id.edtPassword)?.startAnimation(edtAnim)
        v?.findViewById<TextView>(R.id.btnLogin)?.startAnimation(btnLoginAnim)

        v?.findViewById<TextView>(R.id.btnLogin)?.setOnClickListener(this)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_login
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnLogin -> {
                showLoading()
                Handler().postDelayed({
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    activity?.finish()
                }, 3000L)
            }
        }
    }
}