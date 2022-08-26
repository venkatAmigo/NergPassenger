package com.example.nergpassenger.adapters

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.nergpassenger.R
import com.example.nergpassenger.TravelBookingActivity
import com.example.nergpassenger.model.Con

class ConnectionAdapter(val context:Context, val layout: Int, val connections: List<Con>, val
same: Boolean):
    ListAdapter {
    override fun registerDataSetObserver(p0: DataSetObserver?) {

    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getCount(): Int {
        return connections.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =  LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val card = view.findViewById<CardView>(R.id.conOne)
        val dept = view.findViewById<TextView>(R.id.deptTv)
        val arriv = view.findViewById<TextView>(R.id.arrTimeTv)
        val duration = view.findViewById<TextView>(R.id.duartionTv)
        val train = view.findViewById<TextView>(R.id.trainTv)
        val change = view.findViewById<TextView>(R.id.changeOver)

        Log.i("ADAPTER",connections.toString())
        dept.text = connections[position].orgTime
        arriv.text = connections[position].destTime
        duration.text = connections[position].duration
        train.text = connections[position].trainNo
        if(same)
            change.text = "Change Over : 0"
        else
            change.text = "Change Over : 1"

        card.setOnClickListener {
            val intent = Intent(context,TravelBookingActivity::class.java)
            intent.putExtra("Dept",dept.text)
            intent.putExtra("arriv",arriv.text)
            intent.putExtra("duration",duration.text)
            intent.putExtra("train",train.text)
            intent.putExtra("same",same)
            parent.context.startActivity(intent)
        }
        return view
    }

    override fun getItemViewType(p0: Int): Int {
        return p0
    }

    override fun getViewTypeCount(): Int {
        return connections.size
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(p0: Int): Boolean {
        return true
    }
}