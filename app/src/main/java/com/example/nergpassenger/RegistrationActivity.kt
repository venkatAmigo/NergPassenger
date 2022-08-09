package com.example.nergpassenger

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns.EMAIL_ADDRESS
import androidx.core.widget.doOnTextChanged
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityRegistrationBinding
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    var valid = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.login.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        fieldValidation()

        binding.registerBtn.setOnClickListener {
            val username = binding.usernameTl.editText?.text.toString()
            val password = binding.pwdTl.editText?.text.toString()
            val confirm_pwd = binding.confPwdTl.editText?.text.toString()
            if (username.isNullOrBlank() || password.isNullOrBlank() || confirm_pwd.isNullOrBlank()) {

            } else {
                registerUser(username, password)
            }
        }

    }

    fun registerUser(username: String, password: String) {
        Thread {
            val response = Api.register(username, password)
            if (response == 1) {
                val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putBoolean(Constants.IS_LOGIN, true)
                edit.apply()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Handler(mainLooper).post {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Username is already taken")
                    builder.setMessage("Please try another username")
                    builder.show()
                }
            }
        }.start()
    }

    fun fieldValidation() {
        binding.pwdTl.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().length < 8) {
                binding.pwdTl.isErrorEnabled = true
                binding.pwdTl.error = "Password should contain minimum 8 characters"
            } else if (text.isNullOrBlank()) {
                binding.pwdTl.isErrorEnabled = true
                binding.pwdTl.error = "Please enter password"
            } else {
                binding.pwdTl.isErrorEnabled = false
            }
        }
        binding.confPwdTl.editText?.doOnTextChanged { text, start, before, count ->
            if (!text.toString().equals(binding.pwdTl.editText?.text.toString())) {
                binding.confPwdTl.isErrorEnabled = true
                binding.confPwdTl.error = "Password and confirm password should be same"
            } else if (text.isNullOrBlank()) {
                binding.confPwdTl.isErrorEnabled = true
                binding.confPwdTl.error = "Please enter confirm password"
            } else {
                binding.confPwdTl.isErrorEnabled = false

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