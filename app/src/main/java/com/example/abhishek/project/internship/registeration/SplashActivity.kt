package com.example.abhishek.project.internship.registeration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_DELAY: Long = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        val gridExpand = AnimationUtils.loadAnimation(this, R.anim.anim_grid_expand)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.anim_slide_up)


        binding.gridOverlay.visibility = View.VISIBLE
        binding.gridOverlay.startAnimation(gridExpand)

        binding.logoImage.visibility = View.VISIBLE
        binding.logoImage.startAnimation(fadeIn)

        binding.appNameText.visibility = View.VISIBLE
        binding.appNameText.startAnimation(slideUp)


        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}
