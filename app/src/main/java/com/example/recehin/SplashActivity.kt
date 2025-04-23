package com.example.recehin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay untuk menampilkan splash screen selama 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate ke Onboarding Activity
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }, 3000) // 3 detik delay
    }
}