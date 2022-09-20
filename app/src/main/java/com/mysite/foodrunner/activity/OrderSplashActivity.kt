package com.mysite.foodrunner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mysite.foodrunner.R

class OrderSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 4000)
    }

    override fun onPause() {
        super.onPause()
        finishAffinity()
    }
}