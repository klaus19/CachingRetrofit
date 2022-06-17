package com.example.cachingretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cachingretrofit.api.ApiService

class MainActivity : AppCompatActivity() {

    var apiService:ApiService?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






    }
}