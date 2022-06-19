package com.example.cachingretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cachingretrofit.api.ApiService
import com.example.cachingretrofit.databinding.ActivityMainBinding
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class MainActivity : AppCompatActivity() {

    var apiService:ApiService?=null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupRetrofit()

        binding.button.setOnClickListener {

            getRandomJokesAPI()

        }



    }

    private fun setupRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpCacheDirectory = File(cacheDir,"offline-cache")

        //10 MB cache

        val cache:Cache = Cache(httpCacheDirectory,10*1024*1024)

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(provideCacheInterceptor())
    }

    private fun provideCacheInterceptor(): Interceptor {

           return Interceptor(){

           }
    }


}