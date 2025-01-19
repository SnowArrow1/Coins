package com.demo.coins

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.demo.coins.service.Coin
import com.demo.coins.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val listView: ListView = findViewById(R.id.coinsList)
        val toolbar:Toolbar = findViewById(R.id.mytoolbar)
        lifecycleScope.launch {
            val coins = fetchTierOneCoins()
            val coinAdapter = CoinAdapter(this@MainActivity, coins)
            listView.adapter = coinAdapter

            listView.setOnItemClickListener { _, _, position, _ ->
                val clickedCoinName = coins[position].name
                supportActionBar?.title = clickedCoinName
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Currency"


    }

//    ---------------------------------------------------------------


    private suspend fun fetchTierOneCoins(): List<Coin>{
        val call = RetrofitInstance.api.getCoins()
        val coins: MutableList<Coin> = mutableListOf()

        try {
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }
            if (response.isSuccessful) {
                val coinResponse = response.body()
                coinResponse?.data?.coins?.let {
                    for (coin in it) {
                        coins.add(Coin(coin.name, coin.symbol, coin.iconUrl, coin.coinrankingUrl))
                        Log.d("Coin", "Name: ${coin.name}, URL: ${coin.coinrankingUrl},\n Icon: ${coin.iconUrl}")
                    }
                }
            } else {
                Log.e("API Error", response.message())
            }
        } catch (e: Exception) {
            Log.e("API Error", "Failed to fetch coins", e)
        }

        return coins
    }
}