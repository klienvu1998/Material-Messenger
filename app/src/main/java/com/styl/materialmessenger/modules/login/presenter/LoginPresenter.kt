package com.styl.materialmessenger.modules.login.presenter

import android.content.Context
import com.styl.materialmessenger.modules.login.LoginContract
import com.styl.materialmessenger.modules.login.router.LoginRouter

class LoginPresenter(private val loginView: LoginContract.View, private val context: Context?): LoginContract.Presenter {

    private val router = LoginRouter(context)
    override fun navigateToRegister() {
        router.navigateToRegister()
    }

}