package com.example.nergpassenger.adapters

import android.widget.*
import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nergpassenger.R
import com.example.nergpassenger.model.Con

class PassengerAdapter(context:Context, val layout: Int, val connections: List<String>, val price:
Double):
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
        val name = view.findViewById<TextView>(R.id.pass_name_tv)
        val spinner = view.findViewById<Spinner>(R.id.age_spinner)
        val cancelBtn = view.findViewById<ImageView>(R.id.cancel_btn)

        val ages = listOf("6-15","15-26","27-64","65-Above")
        val prices = listOf(price/2,(price*75)/100,price,0)
        val adapter = ArrayAdapter<String>(parent.context,android.R.layout
            .simple_spinner_dropdown_item,ages )
        spinner.adapter = adapter
        name.text = connections[position]
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