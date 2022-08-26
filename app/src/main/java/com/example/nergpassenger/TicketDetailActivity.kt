package com.example.nergpassenger

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.nergpassenger.databinding.ActivityTicketDetailBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class TicketDetailActivity : AppCompatActivity() {
    lateinit var binding :ActivityTicketDetailBinding
    lateinit var origin:String
    lateinit var dest:String
    lateinit var arr:String
    lateinit var dept:String
    lateinit var ticketNo:String
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent =  intent.extras
        origin = intent?.get("origin").toString()
        dest = intent?.get("dest").toString()
        arr = intent?.get("arriv").toString()
        dept = intent?.get("Dept").toString()
        ticketNo = intent?.get("ticket").toString()

        binding.destTv.text = dest
        binding.deptTimeTv.text = arr
        binding.arrivalTimeTv.text = dept
        binding.originTv.text = origin
        binding.ticketNoTv.text = ticketNo

        var metrics = windowManager.maximumWindowMetrics.bounds
        val encoder = BarcodeEncoder()
        var bitmap = encoder.encodeBitmap(Constants.ticket,BarcodeFormat.QR_CODE,metrics.width(),
            metrics.height())
        binding.qrIv.setImageBitmap(bitmap)


    }
}