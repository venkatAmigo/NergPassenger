package com.example.nergpassenger

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false)
        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN,"")
        accessToken?.let { Log.i("token", it) }
        Handler(mainLooper).postDelayed({
            if (isLogin) {
                startActivity(Intent(this, TicketInfoDeskActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}