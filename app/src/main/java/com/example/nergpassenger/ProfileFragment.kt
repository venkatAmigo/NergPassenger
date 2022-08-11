package com.example.nergpassenger

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.example.nergpassenger.api.Api
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*


class ProfileFragment : Fragment() {
    var valid = false
    var personalValid = false
    var cardTypeSelected = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val givenNameEt = view.findViewById<TextInputLayout>(R.id.given_name_et)
        val familyNameEt = view.findViewById<TextInputLayout>(R.id.family_name_et)
        val birthdate = view.findViewById<TextInputLayout>(R.id.birthdate_et)
        val signoutButton = view.findViewById<Button>(R.id.sign_out_btn)
        val spinner = view.findViewById<Spinner>(R.id.cardTypeSpinner)
        val cardTypes = arrayOf("master","visa", "american express")
        val arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout
            .simple_spinner_dropdown_item,cardTypes)
        spinner.adapter = arrayAdapter
        spinner.setSelection(0,true)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                personalValid = true
                cardTypeSelected = cardTypes[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                personalValid = false
            }

        }
        signoutButton.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("LOGIN", Context
                .MODE_PRIVATE)
            val edit = sharedPreferences.edit()
            edit.putBoolean(Constants.IS_LOGIN, false).apply()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            activity?.finish()
        }
        birthdate.setEndIconOnClickListener {
            val calender =  Calendar.getInstance()
            DatePickerDialog(
                requireContext(),{ datePicker, year, month, day ->
                    val dateText = "$year-${String.format("%02d",month+1)}-${String.format("%02d",day)}"
                    birthdate.editText?.setText(dateText)
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),calender.get
                    (Calendar.DAY_OF_MONTH)).show()
        }


        val cardEt = view.findViewById<TextInputLayout>(R.id.cardnumber_et)
        val codeEt = view.findViewById<TextInputLayout>(R.id.security_code_et)
        val expirationEt = view.findViewById<TextInputLayout>(R.id.expiration_et)

        val save = view.findViewById<Button>(R.id.save_btn)
        val savePayment = view.findViewById<Button>(R.id.save_payment_btn)
        savePayment.visibility = View.GONE

        val sharedPreferences = requireContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
        if (accessToken != null) {
            Log.i("response", accessToken)
        }
        Thread {
            val response = accessToken?.let { it1 ->
                Api.getUserDetails(
                    it1, requireContext()
                )
            }
            Log.i("response", response.toString())
            if (!response.isNullOrBlank()) {
                var obj = JSONObject(response.toString())
                Handler(Looper.getMainLooper()).post {
                    givenNameEt.editText?.setText(obj.getString("givenName"))
                    familyNameEt.editText?.setText(obj.getString("familyName"))
                    birthdate.editText?.setText(obj.getString("birthdate"))
                    cardEt.editText?.setText(obj.getString("cardNumber"))
                    expirationEt.editText?.setText(obj.getString("cardExpiration"))
                    codeEt.editText?.setText(obj.getString("cardSecurityCode"))
                }

            }
        }.start()
        save.setOnClickListener {
            if(checkAgeLimit(birthdate.editText?.text.toString()))
            {
                context?.let { it1 -> AlertHelper.showAlert(it1,"Age Error","Age should be at least 14") }

            }else if(personalValid){
                Thread {
                    val response = accessToken?.let { it1 ->
                        Api.updatePersonalDetails(
                            givenNameEt.editText?.text.toString(),
                            familyNameEt.editText?.text.toString(),
                            birthdate.editText?.text.toString(),
                            it1,
                            requireContext()
                        )
                    }
                }.start()
            }
        }
        savePayment.setOnClickListener {
            if(cardEt.editText?.text.toString().isBlank() || expirationEt.editText?.text
                    .toString().isBlank() || codeEt.editText?.text.toString().isBlank
                    ()){
                context?.let { it1 -> AlertHelper.showAlert(it1,"Enter All fields","Please enter all the fields") }
            }else if(valid){
                Thread {
                    val response = accessToken?.let { it1 ->
                        Api.updatePaymentDetails(
                            cardTypeSelected,
                            cardEt.editText?.text.toString(),
                            expirationEt.editText?.text.toString(),
                            codeEt.editText?.text.toString(),
                            it1,
                            requireContext()
                        )
                    }
                }.start()
            }
        }
        save.visibility = View.GONE
        givenNameEt.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                givenNameEt.isErrorEnabled = true
                givenNameEt.error = "Please enter given name"
                personalValid = false
            } else {
                givenNameEt.isErrorEnabled = false
                save.visibility = View.VISIBLE
                personalValid = true
            }
        }
        familyNameEt.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                familyNameEt.isErrorEnabled = true
                familyNameEt.error = "Please enter familyname"
                personalValid = false
            } else {
                familyNameEt.isErrorEnabled = false
                save.visibility = View.VISIBLE
                personalValid = true
            }
        }
        birthdate.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                birthdate.isErrorEnabled = true
                birthdate.error = "Please enter birthdate"
                personalValid = false
            } else {
                birthdate.isErrorEnabled = false
                save.visibility = View.VISIBLE
                personalValid = true
            }
        }
        cardEt.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                cardEt.isErrorEnabled = true
                cardEt.error = "Please enter card number"
                valid = false
            } else if(text.length < 16) {
                cardEt.isErrorEnabled = true
                cardEt.error = "Please enter 16 digit card number"
                valid = false
            }else{
                cardEt.isErrorEnabled = false
                savePayment.visibility = View.VISIBLE
                valid = true
            }
        }
        codeEt.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                codeEt.isErrorEnabled = true
                codeEt.error = "Please enter security code"
                valid = false
            } else {
                codeEt.isErrorEnabled = false
                savePayment.visibility = View.VISIBLE
                valid = true
            }
        }
        expirationEt.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                expirationEt.isErrorEnabled = true
                expirationEt.error = "Please enter expiration"
                valid = false
            } else {
                expirationEt.isErrorEnabled = false
                savePayment.visibility = View.VISIBLE
                valid = true
            }
        }
        return view
    }
    fun checkAgeLimit(birthdate: String):Boolean{
        val values = birthdate.split("-")
        val birth = LocalDate.of(values[0].toInt(), values[1].toInt(),values[2].toInt())
        val age = ChronoUnit.YEARS.between(birth,LocalDate.now())
        return age < 14
    }
}