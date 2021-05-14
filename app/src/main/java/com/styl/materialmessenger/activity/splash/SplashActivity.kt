package com.styl.materialmessenger.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.styl.materialmessenger.R
import com.styl.materialmessenger.activity.BaseActivity
import com.styl.materialmessenger.activity.home.view.HomeActivity
import com.styl.materialmessenger.activity.login.LoginActivity

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
            if (firebaseUser != null) {
                if (firebaseUser?.isEmailVerified == true) {
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
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, 2000L)
    }
}