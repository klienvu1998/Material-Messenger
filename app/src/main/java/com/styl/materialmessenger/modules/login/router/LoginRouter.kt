package com.styl.materialmessenger.modules.login.router

import android.content.Context
import com.styl.materialmessenger.R
import com.styl.materialmessenger.activity.login.LoginActivity
import com.styl.materialmessenger.modules.login.LoginContract
import com.styl.materialmessenger.modules.register.RegisterFragment

class LoginRouter(private val context: Context?): LoginContract.Router {

    override fun navigateToRegister() {
        (context as LoginActivity).supportFragmentManager.beginTransaction()
            .add(R.id.loginContainer, RegisterFragment()).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .addToBackStack(RegisterFragment::class.java.simpleName).commit()
    }

}