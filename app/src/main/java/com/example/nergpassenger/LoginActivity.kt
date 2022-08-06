package com.example.nergpassenger

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.core.widget.doOnTextChanged
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var counter =0
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
        object:CountDownTimer(10000,1000){
            override fun onFinish() {

            }
            override fun onTick(p0: Long) {
                binding.textView4.text=  counter.toString()
                counter++
            }
        }.start()

    }
    fun loginUser(username:String, password:String){
        Thread {
            val response = Api.login(username, password)
            if (response != "INVALID") {
                val resObj = JSONObject(response)
                val accessToken = resObj.getString("access_token")
                val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putBoolean(Constants.IS_LOGIN, true)
                edit.putString(Constants.ACCESS_TOKEN, accessToken)
                edit.apply()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Handler(mainLooper).post{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Invalid credentials")
                    builder.setMessage("Please enter valid username & password")
                    builder.show()
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