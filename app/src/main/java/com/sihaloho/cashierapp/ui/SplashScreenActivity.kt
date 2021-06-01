package com.sihaloho.cashierapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.preference.PrefManager
import com.sihaloho.cashierapp.ui.home.MainActivity
import com.sihaloho.cashierapp.ui.login.LoginActivity
import com.sihaloho.cashierapp.ui.transaction.TransactionActivity

class SplashScreenActivity : AppCompatActivity() {

    private val prefManager by lazy { PrefManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if (prefManager.getBoolean("pref_user_login")){
                when(prefManager.getString("pref_user_level")){
                    "owner" -> {
                        startActivity(Intent(this, TransactionActivity::class.java))

                    }
                    "kasir"->{
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }else{
                startActivity(Intent(this, LoginActivity::class.java))

            }
            finish()
        }, 2000)

    }
}