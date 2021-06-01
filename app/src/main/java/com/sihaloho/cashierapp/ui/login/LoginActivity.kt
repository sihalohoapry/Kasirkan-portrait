package com.sihaloho.cashierapp.ui.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.ActivityLoginBinding
import com.sihaloho.cashierapp.preference.PrefManager
import com.sihaloho.cashierapp.retrofit.ApiConfig
import com.sihaloho.cashierapp.retrofit.response.login.Login
import com.sihaloho.cashierapp.retrofit.response.login.LoginResponse
import com.sihaloho.cashierapp.ui.home.MainActivity
import com.sihaloho.cashierapp.ui.transaction.TransactionActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var progressBar: Dialog? = null
    private val prefManager by lazy { PrefManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListener()
        isLoading()

    }

    private fun isLoading() {
        progressBar = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loading, null)
        progressBar?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

    }

    private fun setUpListener() {
        binding.btnLogin.setOnClickListener {
            if (binding.tvUser.text.isNullOrEmpty() || binding.tvPassword.text.isNullOrBlank()) {
                Toast.makeText(this, "Mohon isi data dengan benar", Toast.LENGTH_SHORT).show()
            } else {
                login(binding.tvUser.text.toString(), binding.tvPassword.text.toString())
            }
        }

    }

    private fun login(userName: String, password: String) {
        progressBar?.show()
        val client = ApiConfig.getApi()
        client.login(userName, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("codeError", response.code().toString())
                response.body()?.data?.level?.let { Log.d("levelUser", it) }
                val su = response.body()?.data?.level
                Log.d("levelUser", su.toString())


                if (response.code() == 200) {
                    progressBar?.dismiss()
                    saveData(response.body()?.data)
                    val data = response.body()?.data?.level
                    if (response.body()?.error == false) {
                        when (data) {
                            "owner" -> {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        TransactionActivity::class.java
                                    )
                                )
                            }
                            "kasir" -> {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                )
                            }
                        }
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Akun tidak terdaftar, mohon hubungi owner",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    progressBar?.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Terjadi Kesalahan pada server",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar?.dismiss()
                Toast.makeText(this@LoginActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun saveData(data: Login?) {
        if (data != null) {
            prefManager.putBoolean("pref_user_login", true)
            prefManager.putString("pref_name_user", data.nama)
            prefManager.putString("pref_user_name", data.username)
            prefManager.putString("pref_user_level", data.level)

        }
    }
}