package com.example.nergpassenger

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nergpassenger.adapters.SeatsAdapter
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityTicketDetailBinding
import com.example.nergpassenger.model.ReservedSeat
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.time.LocalDateTime

class TicketDetailActivity : AppCompatActivity() {
    lateinit var binding :ActivityTicketDetailBinding
    lateinit var origin:String
    lateinit var dest:String
    lateinit var date:String
    lateinit var pass:String
    lateinit var ticketNo:String
    lateinit var seats:String
    lateinit var type:String
    lateinit var train:String
    val selected = mutableListOf<String>()
    lateinit var reservedSeat: List<ReservedSeat>

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent =  intent.extras
        origin = intent?.get("origin").toString()
        dest = intent?.get("dest").toString()
        date = intent?.get("date").toString()
        pass = intent?.get("noofpass").toString()
        ticketNo = intent?.get("ticket").toString()
        seats = intent?.get("seats").toString()
        type = intent?.get("type").toString()
        train = intent?.get("train").toString()
        Log.i("TRAIN",train)
        reservedSeat = mutableListOf()
        if(type == "regional"){
            binding.noReservationTv.visibility = View.VISIBLE
        }
        else{
            binding.noReservationTv.visibility = View.GONE
        }
        binding.destTv.text = dest
        binding.dateTv.text = date
        binding.passTv.text = pass
        binding.originTv.text = origin
        binding.ticketNoTv.text = ticketNo
        binding.seatsTv.text = seats
        getSeatReservations()

        val railcars = listOf("Railcar 1","Railcar 2","Railcar 3","Railcar 4","Railcar 5")
        binding.railcarSpinner.adapter =  ArrayAdapter<String>(this,android.R.layout
            .simple_spinner_dropdown_item,railcars)
        binding.seatsRecycler.layoutManager = GridLayoutManager(this@TicketDetailActivity,3,RecyclerView
            .HORIZONTAL,false)
        binding.railcarSpinner.setSelection(0)
        binding.railcarSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectReser = reservedSeat.filter { it.railroadCarNumber == p2+1 }
                val selectSeats = mutableListOf<String>()
                for(i in selectReser){
                    selectSeats.add(i.seatNumber.toString())
                }
                val adapter = SeatsAdapter(selectSeats,p2+1)
                binding.seatsRecycler.layoutManager = GridLayoutManager(this@TicketDetailActivity,3,RecyclerView
                    .VERTICAL,false)
                binding.seatsRecycler.adapter = adapter
                adapter.notifyDataSetChanged()
//                val childrens = binding.seatsRecycler.children
//                val selected = mutableListOf<String>()
//                for(i in childrens){
//                    val card = i.findViewById<CardView>(R.id.seats_card)
//                    val seat = i.findViewById<TextView>(R.id.seat_no_tv)
//                    if(card.cardBackgroundColor.equals(Color.GREEN)){
//                        Log.i("Selected",seat.text.toString())
//                        selected.add(seat.text.toString())
//                    }
//                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }
    fun selectedSeatsMethod(carNo:Int, seat: String){
        selected.add(seat)
    }

    fun getSeatReservations(){
        Thread{
            val resrvRes = Api.getReservedSeats(this, train)
            val resrvObj = JSONArray(resrvRes).toString()
            val reservations = Json.decodeFromString<List<ReservedSeat>>(resrvObj)
            Handler(mainLooper).post {
                reservedSeat = reservations
            }
        }.start()
    }
}