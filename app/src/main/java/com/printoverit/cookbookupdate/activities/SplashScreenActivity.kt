package com.printoverit.cookbookupdate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.printoverit.cookbookupdate.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreenTheme)
        setContentView(R.layout.activity_splash_screen)
    }
}