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

        binding.register.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
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
    fun startTimer(){
        object:CountDownTimer(10000,1000){
            override fun onFinish() {
                binding.loginBtn.isEnabled = true
                binding.timerTv.visibility = View.GONE
                binding.waitForTv.visibility = View.GONE
                val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putInt("FAILED_COUNT",0).apply()
                counter = 10
            }
            override fun onTick(p0: Long) {
                val seconds = "$counter Seconds"
                binding.timerTv.text=  seconds
                counter --
            }
        }.start()
    }
    fun loginUser(username:String, password:String){
        val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val failedCount = sharedPreferences.getInt("FAILED_COUNT",0)
        if(failedCount >= 3){
            startTimer()
            binding.loginBtn.isEnabled = false
            binding.timerTv.visibility = View.VISIBLE
            binding.waitForTv.visibility = View.VISIBLE

        }else {
            Thread {
                val response = Api.login(username, password)
                if (response != "INVALID") {
                    val resObj = JSONObject(response)
                    val accessToken = resObj.getString("access_token")
                    edit.putBoolean(Constants.IS_LOGIN, true)
                    edit.putString(Constants.ACCESS_TOKEN, accessToken)
                    edit.apply()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    edit.putInt("FAILED_COUNT", failedCount + 1).apply()
                    Handler(mainLooper).post {
                        AlertHelper.showAlert(this,"Invalid credentials","Please enter valid username & password")
                    }
                }
            }.start()
        }
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