package com.example.nergpassenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nergpassenger.adapters.PagerAdapter
import com.example.nergpassenger.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = PagerAdapter(supportFragmentManager,lifecycle)
        TabLayoutMediator(binding.tabLt,binding.viewPager){
            tab,position->
            if(position==0)
                tab.text = "Tickets Info"
            else
                tab.text = "User Info"
        }.attach()
    }
}