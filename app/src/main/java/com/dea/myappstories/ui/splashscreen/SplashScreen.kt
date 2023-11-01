package com.dea.myappstories.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.dea.myappstories.R
import com.dea.myappstories.databinding.ActivitySplashScreenBinding
import com.dea.myappstories.ui.ViewModelFactory
import com.dea.myappstories.ui.main.MainActivity
import com.dea.myappstories.ui.welcome.WelcomeActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels { ViewModelFactory.getInstance(application) }
    private val splashScreen = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.title1.text = getString(R.string.story)
        binding.desc1.text = getString(R.string.description)

        splashScreenViewModel.getSession().observe(this@SplashScreen){
            if (!it.isLogin){
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashScreen, WelcomeActivity::class.java))
                    finish()
                }, splashScreen)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                    finish()
                }, splashScreen)
            }
        }
    }
}