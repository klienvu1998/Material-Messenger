package com.styl.materialmessenger.modules.login

import android.os.Bundle
import android.view.View
import com.styl.materialmessenger.BaseActivity
import com.styl.materialmessenger.R

class LoginActivity: BaseActivity(), View.OnClickListener {

    lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.loginContainer, loginFragment).commit()
    }

    override fun onClick(p0: View?) {

    }
}