package com.demo.coins

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.demo.coins.service.ApiService
import com.demo.coins.service.CoinData
import com.demo.coins.service.CoinResponse
import com.demo.coins.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchTierOneCoins()
    }

    private fun fetchTierOneCoins(){
        val call = RetrofitInstance.api.getCoins()

        call.enqueue(object : Callback<CoinResponse>{
            override fun onResponse(
                call: Call<CoinResponse>,
                response: Response<CoinResponse>
            ) {
                if (response.isSuccessful) {
                    val coinResponse = response.body()
                    coinResponse?.data?.coins?.let {
                        for (coin in it) {
                            Log.d("Coin", "Name: ${coin.name}, Price: ${coin.price}")
                        }
                    }
                } else {
                    Log.e("API Error", response.message())
                }
            }

            override fun onFailure(call: Call<CoinResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch coins", t)
            }

        })
    }
}