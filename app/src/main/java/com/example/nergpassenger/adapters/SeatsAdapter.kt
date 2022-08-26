package com.example.nergpassenger.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nergpassenger.AlertHelper
import com.example.nergpassenger.R
import com.example.nergpassenger.TicketDetailActivity
import com.example.nergpassenger.model.Ticket

class SeatsAdapter(val selectedSeats: List<String>, railCarNumber: Int ):
    RecyclerView
.Adapter<SeatsAdapter
.SeatsHolder>
    () {
    lateinit var context: Context
    var isRed = false
    var seats:List<String> = listOf("1A","2A","3A","4A","5A","6A","7A","8A","9A","10A",
    "11A","12A","13A","14A","15A","16A","17A","18A","19A","20A",
    "21A","22A","23A","24A","25A","26A","27A","28A")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatsHolder {
        context = parent.context
        return SeatsHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.railcar_seat_item,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SeatsHolder, position: Int) {
        isRed = selectedSeats.contains(seats[position])
        holder.bind(seats,selectedSeats, position, context,isRed)
    }

    override fun getItemCount(): Int {
        return seats.size
    }

    class SeatsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.seats_card)
        val seatsTv: TextView = itemView.findViewById(R.id.seat_no_tv)

        fun bind(seats: List<String>, selectedSeats: List<String>, pos: Int, context: Context,
                 isRed:Boolean) {
            if(selectedSeats.contains(seats[pos])) {
                card.setCardBackgroundColor(Color.RED)
            }
            seatsTv.text = seats[pos]

            card.setOnClickListener {
                if(!isRed){
                    card.setCardBackgroundColor(Color.GREEN)
                }
            }
        }

    }

}