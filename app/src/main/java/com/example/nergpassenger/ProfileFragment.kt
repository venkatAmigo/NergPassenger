package com.example.nergpassenger

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import com.example.nergpassenger.api.Api
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val givenNameEt = view.findViewById<TextInputLayout>(R.id.given_name_et)
        val familyNameEt = view.findViewById<TextInputLayout>(R.id.family_name_et)
        val birthdate = view.findViewById<TextInputLayout>(R.id.birthdate_et)

        val cardEt = view.findViewById<TextInputLayout>(R.id.cardnumber_et)
        val codeEt = view.findViewById<TextInputLayout>(R.id.security_code_et)
        val expirationEt = view.findViewById<TextInputLayout>(R.id.expiration_et)

        val save = view.findViewById<Button>(R.id.save_btn)
        val savePayment = view.findViewById<Button>(R.id.save_payment_btn)
        savePayment.visibility =View.GONE

        val sharedPreferences = requireContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
        if (accessToken != null) {
            Log.i("response",accessToken)
        }
        Thread{
            val response = accessToken?.let { it1 ->
                Api.getUserDetails(
                    it1,requireContext()
                )
            }
            Log.i("response",response.toString())
            if(!response.isNullOrBlank()) {
                var obj = JSONObject(response.toString())
                Handler(Looper.getMainLooper()).post{
                    givenNameEt.hint = obj.getString("givenName")
                    familyNameEt.hint = obj.getString("familyName")
                    birthdate.hint = obj.getString("birthdate")
                    cardEt.hint = obj.getString("cardNumber")
                    expirationEt.hint = obj.getString("cardExpiration")
                    codeEt.hint = obj.getString("cardSecurityCode")
                }

            }
        }.start()
        save.setOnClickListener {
            Thread{
                val response = accessToken?.let { it1 ->
                    Api.updatePersonalDetails(givenNameEt.editText?.text.toString(),
                        familyNameEt.editText?.text.toString(),birthdate.editText?.text.toString(),
                        it1,requireContext()
                    )
                }
            }.start()
        }
        savePayment.setOnClickListener {
            Thread{
                val response = accessToken?.let { it1 ->
                    Api.updatePaymentDetails("master",cardEt.editText?.text.toString(),
                        expirationEt.editText?.text.toString(),codeEt.editText?.text.toString(),
                        it1,requireContext()
                    )
                }
            }.start()
        }
        save.visibility = View.GONE
        givenNameEt.editText?.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                givenNameEt.isErrorEnabled = true
                givenNameEt.error ="Please enter given name"
            }else{
                givenNameEt.isErrorEnabled = false
                save.visibility = View.VISIBLE
            }
        }
        familyNameEt.editText?.doOnTextChanged { text, start, before, count ->
           if(text.isNullOrBlank()){
                familyNameEt.isErrorEnabled = true
                familyNameEt.error ="Please enter familyname"
            }else{
                familyNameEt.isErrorEnabled = false
               save.visibility = View.VISIBLE
            }
        }
        birthdate.editText?.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                birthdate.isErrorEnabled = true
                birthdate.error ="Please enter birthdate"
            }else{
                birthdate.isErrorEnabled = false
                save.visibility = View.VISIBLE
            }
        }
        cardEt.editText?.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                cardEt.isErrorEnabled = true
                cardEt.error ="Please enter card number"
            }else{
                cardEt.isErrorEnabled = false
                savePayment.visibility = View.VISIBLE
            }
        }
        codeEt.editText?.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                codeEt.isErrorEnabled = true
                codeEt.error ="Please enter security code"
            }else{
                codeEt.isErrorEnabled = false
                savePayment.visibility = View.VISIBLE
            }
        }
        expirationEt.editText?.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                expirationEt.isErrorEnabled = true
                expirationEt.error ="Please enter expiration"
            }else{
                expirationEt.isErrorEnabled = false
                savePayment.visibility = View.VISIBLE
            }
        }
        return view
    }
}