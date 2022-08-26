package com.example.nergpassenger

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var counter = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fieldValidation()
        binding.loginBtn.setOnClickListener {
            val username = binding.usernameTl.editText?.text.toString()
            val password = binding.pwdTl.editText?.text.toString()
            if(username.isNullOrBlank() || password .isNullOrBlank()){

            }else{
                loginUser(username,password)
            }
        }


    }
    fun loginUser(username:String, password:String){
        val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val failedCount = sharedPreferences.getInt("FAILED_COUNT",0)
            Thread {
                val response = Api.login(username, password)
                if (response != "INVALID") {
                    val resObj = JSONObject(response)
                    val accessToken = resObj.getString("access_token")
                    edit.putBoolean(Constants.IS_LOGIN, true)
                    edit.putString(Constants.ACCESS_TOKEN, accessToken)
                    edit.apply()
                    startActivity(Intent(this,TicketsActivity::class.java))
                } else {
                    edit.putInt("FAILED_COUNT", failedCount + 1).apply()
                    Handler(mainLooper).post {
                        AlertHelper.showAlert(this,"Invalid credentials","Please enter valid username & password")
                    }
                }
            }.start()
    }
    fun fieldValidation() {
        binding.pwdTl.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                binding.pwdTl.isErrorEnabled = true
                binding.pwdTl.error = "Please enter password"
            } else {
                binding.pwdTl.isErrorEnabled = false
            }
        }
        binding.usernameTl.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                binding.usernameTl.isErrorEnabled = true
                binding.usernameTl.error = "Please enter username"
            } else {
                binding.usernameTl.isErrorEnabled = false
            }
        }
    }
}