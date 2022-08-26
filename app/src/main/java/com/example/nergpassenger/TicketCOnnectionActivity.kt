package com.example.nergpassenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import com.example.nergpassenger.adapters.ConnectionAdapter
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityTicketConnectionBinding
import com.example.nergpassenger.databinding.ActivityTicketInfoDeskBinding
import com.example.nergpassenger.model.Con
import com.example.nergpassenger.model.Connection
import com.example.nergpassenger.model.Line
import com.example.nergpassenger.model.LineDetail
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.time.Duration
import java.time.LocalTime

class TicketCOnnectionActivity : AppCompatActivity() {
    var arrivalLine = ""
    var destLine = ""
    lateinit var binding: ActivityTicketConnectionBinding
    val hashLinesCodes: HashMap<String,List<String>> = HashMap()
    lateinit var origin:String
    lateinit var dest:String
    lateinit var arrDept:String
    lateinit var time:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent =  intent.extras
        origin = intent?.get("ORIGIN").toString()
        dest = intent?.get("DEST").toString()
        arrDept = intent?.get("ARR_OT_DEPT").toString()
        time = intent?.get("TIME").toString()

        val line11 = listOf("NB","NZ","LOE","OR","B","BER")
        val line12 = listOf("BER","B","OR","LOE","NZ","NB")
        val line21 = listOf("NB","MC","GUE","SN","HH")
        val line22 = listOf("HH","SN","GUE","MC","NB")
        val line2a1 = listOf("NB","MC","GUE","HRO")
        val line2a2 = listOf("HRO","GUE","MC","NB")
        val line31 = listOf("NB","AT","DM","HST","HRO")
        val line32 = listOf("HRO","HST","DM","AT","NB")
        val line41 = listOf("NB","PAS","SCZ")
        val line42 = listOf("SCZ","PAS","NB")
        val allLines = listOf(line12,line11,line22,line21,line2a2,line2a1,line32,line31,line42,
            line41)

        Thread{
            val linesRes = Api.getLines(this)
            val linesObj = JSONArray(linesRes).toString()
            val lines = Json.decodeFromString<List<Line>>(linesObj)
            var counter = 0
            lines.forEach {
                hashLinesCodes[it.id] = allLines[counter]
                counter++
            }
            var same = false
            hashLinesCodes.forEach {
                if(arrivalLine == "" && destLine ==""){
                    if(it.value.contains(origin) && it.value.contains(dest) ){
                       if(it.value.indexOf(origin) < it.value.indexOf(dest)){
                        arrivalLine = it.key
                        destLine = it.key
                        same =  true
                        }
                    }
                }
            }
            if(!same){
                hashLinesCodes.forEach {
                    if(arrivalLine == "" && it.value.contains(origin) && it.value.indexOf(origin)< it
                            .value.indexOf("NB"))
                        arrivalLine = it.key
                    if(destLine == "" && it.value.contains(dest) && it.value.indexOf(dest) > it
                            .value.indexOf("NB"))
                        destLine = it.key
                }
            }
            Log.i("INElLIN",arrivalLine+" "+destLine)

            if(same) {
                val lineConRes = Api.getLines(this, arrivalLine)
                val lineConObj = JSONObject(lineConRes).toString()
                val lineCons = Json.decodeFromString<LineDetail>(lineConObj)
                val five = getSameLinesConnections(lineCons)
                val cons = getFiveDetails(five)
                Handler(mainLooper).post {
                    val adapter = ConnectionAdapter(this,R.layout.connections_item,cons, true)
                    binding.connectionsList.adapter = adapter
                }
            }else{
                val originPartObj = JSONObject(Api.getLines(this, arrivalLine)).toString()
                val originPartCons = Json.decodeFromString<LineDetail>(originPartObj)
                val destPartObj = JSONObject(Api.getLines(this, destLine)).toString()
                val destPartCons = Json.decodeFromString<LineDetail>(destPartObj)

                val validOriginCon = getOnlyOriginCons(originPartCons.connections)
                var count = 0
                val finalConnections = mutableListOf<Con>()
                validOriginCon.forEach {
                    var fromNbCons: List<Connection>
                        val stop = it.stops?.filter { stop ->
                            stop.station
                                .code == "NB"
                        }
                        fromNbCons =
                            stop?.get(0)?.let { it1 -> getOnlyDesCons(destPartCons.connections, it1.time) }!!
                        fromNbCons.forEach {
                            dest ->
                            if(count < 6){
                                finalConnections.add(getDetailsForNotSame(it,dest))
                                count++
                            }
                        }
                }
                Handler(mainLooper).post {
                    val adapter = ConnectionAdapter(this,R.layout.connections_item,
                        finalConnections, false)
                    binding.connectionsList.adapter = adapter
                }
            }
        }.start()
    }
    fun getOnlyOriginCons(connections:List<Connection>): List<Connection> {
        return connections.filter {
            it.stops!!.any { stop -> stop.station.code == origin && LocalTime.parse(stop.time)
            .isAfter(LocalTime.parse(time))
            }
        }
    }
    fun getOnlyDesCons(connections:List<Connection>,fromNbTime: String): List<Connection> {
        return connections.filter {
            it.stops!!.any { stop -> stop.station.code == "NB" && LocalTime.parse(stop.time)
                .isAfter(LocalTime.parse(fromNbTime))
            }
        }
    }
    fun getSameLinesConnections(lineDetail: LineDetail): List<Connection> {
        val validConnections= mutableListOf<Connection>()
        lineDetail.connections.forEach {
            var both = 0
            it.stops!!.forEach { stop ->
                if(stop.station.code == origin)
                    both++
                if(stop.station.code == dest)
                    both++
            }
            if(both == 2)
                validConnections.add(it)
        }
        val code: String = if(arrDept == "Departure Time")
            origin
        else
            dest
        val arrConnections = validConnections.filter {
            it.stops!!.any { stop ->
                var con = if(code == origin){
                    LocalTime.parse(stop.time).isAfter(LocalTime.parse(time))
                } else{
                    LocalTime.parse(stop.time).isBefore(LocalTime.parse(time))
                }
                stop.station.code == code && con
            }
        }
        Log.i("GALTHI",Json.encodeToString(arrConnections))
        return arrConnections.take(5)
    }
    fun getFiveDetails(five : List<Connection>): MutableList<Con> {
        val cons = mutableListOf<Con>()
        five.forEach {
            var originTime = ""
            var destTime = ""
            it.stops!!.forEach { stop ->
                if(originTime == "" && stop.station.code == origin)
                    originTime = stop.time
                if(destTime == "" && stop.station.code == dest)
                    destTime = stop.time
            }
            val duration = Duration.between(LocalTime.parse(destTime),LocalTime.parse(originTime))
            val co = Con(originTime, destTime, duration.toString().removeRange(0,3), it.trainNumber)
            cons.add(co)
        }
        return cons
    }
    fun getDetailsForNotSame(first : Connection, second: Connection): Con {
        var firstOriginTime = ""
        var firstDestTime = ""
        first.stops!!.forEach { stop ->
                if(firstOriginTime == "" && stop.station.code == origin)
                    firstOriginTime = stop.time
                if(firstDestTime == "" && stop.station.code == "NB")
                    firstDestTime = stop.time
            }
        var secondOriginTime = ""
        var secondDestTime = ""
        second.stops!!.forEach { stop ->
            if(secondOriginTime == "" && stop.station.code == "NB")
                secondOriginTime = stop.time
            if(secondDestTime == "" && stop.station.code == dest)
                secondDestTime = stop.time
        }
            val durationOne = Duration.between(LocalTime.parse(firstOriginTime),LocalTime.parse
                (firstDestTime))
            val durationTwo = Duration.between(LocalTime.parse(firstOriginTime),LocalTime.parse
            (firstDestTime))

            val co = Con(firstOriginTime, secondDestTime, durationOne.toString().removeRange(0,2)
                    + " + "+durationTwo.toString().removeRange(0,2),
                first
                .trainNumber + " -> "+ second.trainNumber)
        return co
        }

}