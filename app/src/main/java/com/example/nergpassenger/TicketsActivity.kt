package com.example.nergpassenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nergpassenger.adapters.TicketsAdapter
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityTicketsBinding
import com.example.nergpassenger.model.Ticket
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.time.LocalDate

class TicketsActivity : AppCompatActivity() {

    lateinit var binding: ActivityTicketsBinding
    lateinit var adapter: TicketsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)
       binding.ticketsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView
        .VERTICAL,
            false)
        getTicketsData()
        binding.signOutBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("LOGIN", Context
                .MODE_PRIVATE)
            val edit = sharedPreferences.edit()
            edit.putBoolean(Constants.IS_LOGIN, false).apply()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
    fun getTicketsData(){
        val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
        if (accessToken != null) {
            Log.i("response",accessToken)
        }
        Thread{
            val response = accessToken?.let { it1 ->
                Api.getUserTickets(
                    it1,this
                )
            }
            if(!response.isNullOrBlank()) {
                var obj = JSONArray(response.toString())
                val tickets = Json.decodeFromString<List<Ticket>>(obj.toString())
                val newTickets = tickets.sortedByDescending { t -> t.travelDate }
                //Log.i("TICKETS",tickets[1].toString())
                val currentFutureTickets = newTickets.filter { getStatus(it.travelDate) }
                Handler(Looper.getMainLooper()).post{
                    adapter = TicketsAdapter(currentFutureTickets)
                    binding.ticketsRecycler.adapter = adapter
                }

            }
        }.start()
    }
    fun getStatus(time: String):Boolean{
        val dateTime = LocalDate.parse(time)
        val current = LocalDate.now()
        return dateTime.isAfter(current) || dateTime.isEqual(current)
    }
}