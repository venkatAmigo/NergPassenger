package com.example.nergpassenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nergpassenger.R
import com.example.nergpassenger.adapters.TicketsAdapter.*
import com.example.nergpassenger.model.Ticket
import com.example.nergpassenger.model.Tickets
import java.time.LocalDate
import java.time.LocalDateTime

class TicketsAdapter(val tickets: List<Ticket>) : RecyclerView.Adapter<TicketsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsHolder {
        return TicketsHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ticket_item,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TicketsHolder, position: Int) {
        holder.bind(tickets, position)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    class TicketsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val origin: TextView = itemView.findViewById(R.id.origin_tv)
        val dest: TextView = itemView.findViewById(R.id.dest_tv)
        val arrivalTime: TextView = itemView.findViewById(R.id.arrival_time_tv)
        val deptTime: TextView = itemView.findViewById(R.id.dept_time_tv)
        val status: TextView = itemView.findViewById(R.id.status_tv)
        val ticketNo: TextView = itemView.findViewById(R.id.ticket_no_tv)

        fun bind(ticketsList: List<Ticket>, pos: Int) {
            origin.text = ticketsList[pos].segments[0].departureStop.station.name
            dest.text = ticketsList[pos].segments[0].arrivalStop.station.name
            val depTime = "${ticketsList[pos].travelDate} ${
                ticketsList[pos].segments[0]
                    .departureStop.time
            }"
            val arrTime = "${ticketsList[pos].travelDate} ${
                ticketsList[pos].segments[0]
                    .arrivalStop.time
            }"
            deptTime.text = depTime
            arrivalTime.text = arrTime
            ticketNo.text = ticketsList[pos].ticketNumber
            val statusString = getStatus(ticketsList[pos].travelDate)
            if(statusString == "Upcoming")
            status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tommorow,0);
            else if(statusString == "Past")
                status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.past,0);
            else
                status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.today,0);
            status.text = statusString
        }

        fun getStatus(time: String):String{
            val dateTime = LocalDate.parse(time)
            val current = LocalDate.now()
            if(dateTime.isAfter(current))
                return "Upcoming"
            if(dateTime.isBefore(current))
                return "Past"
            return "Current"
        }

    }

}