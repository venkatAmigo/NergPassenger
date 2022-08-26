package com.example.nergpassenger

import android.content.Intent
import android.database.DataSetObserver
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import androidx.core.view.children
import com.example.nergpassenger.adapters.PassengerAdapter
import com.example.nergpassenger.databinding.ActivityTravelBookingBinding

class TravelBookingActivity : AppCompatActivity() {
    lateinit var binding: ActivityTravelBookingBinding
    lateinit var passList:MutableList<String>
    var price: Double = 0.15
    var km:Int = 50
    var same = true
    lateinit var dept:String
    lateinit var arr:String
    lateinit var duration:String
    lateinit var train:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTravelBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent =  intent.extras
        dept = intent?.get("Dept").toString()
        arr = intent?.get("arriv").toString()
        duration= intent?.get("duration").toString()
        train = intent?.get("train").toString()
        same = intent?.getBoolean("same", false)!!
        if(same)
            binding.changeOver.text = "Change Over : 0"
        else
            binding.changeOver.text = "Change Over : 1"

        binding.deptTv.text = dept.toString()
        binding.arrTimeTv.text = arr
        binding.duartionTv.text = duration
        binding.trainTv.text = train


        binding.loyaltySpinner.adapter = ArrayAdapter<String>(this,android.R.layout
            .simple_spinner_dropdown_item,
        listOf("25%","50%"," No loyalty card"))

        binding.button.setOnClickListener {
            val no = binding.noOfPassTv.text.toString().toInt()
            passList = mutableListOf()
            passList.clear()
            for( i in 1..no){
                passList.add("Passenger $i")
            }
            val adapter = PassengerAdapter(this, R.layout.passenger_item, passList,price)
            binding.ageList.adapter = adapter

        }
        var isRegional = false
        binding.radioGroup.check(R.id.regRad)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            var btn = radioGroup.findViewById<RadioButton>(i)
            if(btn.text == "Regional")
                isRegional = true

        }
        binding.confirmBtn.setOnClickListener {
            AlertHelper.showAlert(this,"Confirm","Do u confirm tickets")
            startActivity(Intent(this, TicketsActivity::class.java))
        }
        binding.loyaltySpinner.setSelection(2)
        binding.getPriceBTn.setOnClickListener {
            val items = binding.ageList.children
            val prices = mutableListOf<Int>()
            items.forEach {
                val spinner = it.findViewById<Spinner>(R.id.age_spinner)
                val age = spinner.selectedItem
                Log.i("PRICE",age.toString())
                val ages = listOf("6-15","15-26","27-64","65-Above")
                if(age == ages[0])
                    prices.add(50)
                else if(age == ages[1])
                    prices.add((75))
                else if(age == ages[2])
                    prices.add(0)
                else
                    prices.add(100)
            }
            var totalNormalPrice = getPrice(passList.size,false,prices)
            var totalRegionalPrice = getPrice(passList.size,true,prices)
            binding.normalPriceTv.text = totalNormalPrice.toString()
            binding.regionalPrice.text = totalRegionalPrice.toString()
            var loyal = 0
            binding.loyaltySpinner.onItemSelectedListener = object : AdapterView
            .OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if(p2 == 0) {
                        binding.normalPriceTv.text = ((totalNormalPrice * 25) / 100).toString()
                        binding.regionalPrice.text = ((totalRegionalPrice * 25) / 100).toString()
                    }
                    else if(p2 == 1) {
                        binding.normalPriceTv.text = ((totalNormalPrice * 50) / 100).toString()
                        binding.regionalPrice.text = ((totalRegionalPrice * 25) / 100).toString()
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        }
    }
    fun getPrice(no: Int, isRegional :Boolean,ages: List<Int>): Double {
        if(isRegional)
        {
            val tickets = no/3
            if(same)
            return ((tickets+1) * 40).toDouble()
            else
                return 2*((tickets+1) * 40).toDouble()
        }
        var total =0.0
        ages.forEach {
            Log.i("PRICE",it.toString())
            total += km*(price - (price*it)/100)
        }
        return total
    }
}