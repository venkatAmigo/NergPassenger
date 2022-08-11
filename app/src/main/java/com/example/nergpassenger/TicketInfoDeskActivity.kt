package com.example.nergpassenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.RadioButton
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.nergpassenger.api.Api
import com.example.nergpassenger.databinding.ActivityTicketInfoDeskBinding
import com.example.nergpassenger.model.Station
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray

class TicketInfoDeskActivity : AppCompatActivity() ,OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    lateinit var map:GoogleMap
    lateinit var binding: ActivityTicketInfoDeskBinding
    var hashMarkerMap: HashMap<String, Marker> = HashMap()
    var stationCodesMap: HashMap<String, String> = HashMap()
    var origin = ""
    var prevOrigin = ""
    var dest = ""
    var prevDest = ""
    var arDepTime = "Departure Time"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketInfoDeskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapsFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.container_view,mapsFragment).commit()

        mapsFragment.getMapAsync {
            map = it
            Thread{
                val stationsRes = Api.getStations(this)
                Handler(mainLooper).post {
                    val stationsObj = JSONArray(stationsRes).toString()
                    val stations = Json.decodeFromString<List<Station>>(stationsObj)

                    stations.forEach { station ->
                        stationCodesMap.put(station.name,station.code)
                        var markerOptions =
                            MarkerOptions().position(LatLng(station.latitude, station.longitude))
                                .title(station.name).anchor(0.5f, 0.5f).icon(
                                    BitmapDescriptorFactory
                                        .defaultMarker(HUE_GREEN)
                                )
                        val marker = map.addMarker(markerOptions)!!
                        hashMarkerMap[station.name] = marker
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    station.latitude,
                                    station.longitude
                                ), map.maxZoomLevel
                            )
                        )
                        map.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    station.latitude,
                                    station.longitude
                                )
                            )
                        )
                    }
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                stations[stations
                                    .size - 1].latitude, stations[stations.size - 1].longitude
                            ), 6.0f
                        )
                    )
                    map.setOnMarkerClickListener(this)
                }
            }.start()
        }
        binding.radioGroup.check(R.id.deptRadioButton)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val btn =  radioGroup.findViewById<RadioButton>(i)
            arDepTime = btn.text.toString()
        }

        binding.gotoConBtn.setOnClickListener {
            val intent  = Intent(this,TicketCOnnectionActivity::class.java)
            intent.putExtra("ORIGIN",stationCodesMap[origin])
            intent.putExtra("DEST",stationCodesMap[dest])
            intent.putExtra("ARR_OT_DEPT",arDepTime)
            startActivity(intent)
        }
        binding.signOutBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("LOGIN", Context
                .MODE_PRIVATE)
            val edit = sharedPreferences.edit()
            edit.putBoolean(Constants.IS_LOGIN, false).apply()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap) {

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val title = p0.title.toString()
        if(origin == ""){
            origin = title
            prevOrigin = title
            p0.snippet = "Origin"
            p0.setIcon(defaultMarker(HUE_BLUE))
            binding.originTv.text = origin
        }else if(dest == "" && title != prevOrigin){
            dest = title
            prevDest = title
            p0.snippet = "Destination"
            p0.setIcon(defaultMarker(HUE_RED))
            binding.destTv.text = dest
        }else{
            if(title != prevOrigin){
                dest = ""
                val prevOM = hashMarkerMap[prevOrigin]
                prevOM?.snippet = ""
                prevOM?.setIcon(defaultMarker(HUE_GREEN))
                val prevDM = hashMarkerMap[prevDest]
                prevDM?.snippet = ""
                prevDM?.setIcon(defaultMarker(HUE_GREEN))
                origin = title
                prevOrigin = title
                p0.snippet = "Origin"
                p0.setIcon(defaultMarker(HUE_BLUE))
                binding.originTv.text = origin
                binding.destTv.text = "Select Destination"
            }
        }
        return false
    }
}