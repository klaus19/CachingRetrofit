package com.example.cachingretrofit


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cachingretrofit.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@MainActivity,SecondActivity::class.java))
        }



    }



//
//    //checking about network connectivity
//    fun hasNetwork(context: Context): Boolean? {
//        var isConnected: Boolean? = false
//        var connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            val network = connectivityManager.activeNetwork ?: return false
//
//            //Representation of capabilities of an active network
//            val activeNetwork = connectivityManager.getNetworkCapabilities(network)
//
//            if (activeNetwork != null) {
//                return when {
//                    // Indicates this network uses a Wi-Fi transport,
//                    // or WiFi has network connectivity
//                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                    // Indicates this network uses a Cellular transport. or
//                    // Cellular has network connectivity
//                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//
//                    // else return false
//                    else -> false
//                }
//            } else {
//                @Suppress("DEPRECATION") val networkInfo =
//                    connectivityManager.activeNetworkInfo ?: return false
//                @Suppress("DEPRECATION")
//                return networkInfo.isConnected
//            }
//        }
//
//          return isConnected
//    }



}