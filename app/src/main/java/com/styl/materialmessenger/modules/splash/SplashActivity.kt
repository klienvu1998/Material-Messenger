package com.styl.materialmessenger.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.styl.materialmessenger.modules.BaseActivity
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.home.view.HomeActivity
import com.styl.materialmessenger.modules.login.LoginActivity

class SplashActivity: BaseActivity() {

    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
            if (firebaseUser != null) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, 3000L)
    }
}