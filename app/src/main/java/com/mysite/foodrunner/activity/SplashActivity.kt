package com.mysite.foodrunner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mysite.foodrunner.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            var startAct = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(startAct)
        }, 300)
    }

    override fun onPause() {
        super.onPause()
        finishAffinity()
    }
}