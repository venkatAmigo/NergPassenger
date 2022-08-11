package com.example.nergpassenger

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nergpassenger.adapters.TicketsAdapter
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.model.Ticket
import com.example.nergpassenger.model.Tickets
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject

class TicketsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TicketsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tickets, container, false)
        recyclerView = view.findViewById(R.id.tickets_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,
            false)
        getTicketsData()
        return view
    }
    fun getTicketsData(){
        val sharedPreferences = requireContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
        if (accessToken != null) {
            Log.i("response",accessToken)
        }
        Thread{
            val response = accessToken?.let { it1 ->
                Api.getUserTickets(
                    it1,requireContext()
                )
            }
            if(!response.isNullOrBlank()) {
                var obj = JSONArray(response.toString())
                val tickets = Json.decodeFromString<List<Ticket>>(obj.toString())
                val newTickets = tickets.sortedByDescending { t -> t.travelDate }
                Log.i("TICKETS",tickets[1].toString())
                Handler(Looper.getMainLooper()).post{
                    adapter = TicketsAdapter(newTickets)
                    recyclerView.adapter = adapter
                }

            }
        }.start()
    }
}