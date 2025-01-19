package com.demo.coins


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Picture
import android.graphics.drawable.PictureDrawable
import com.caverock.androidsvg.SVG
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.demo.coins.service.Coin


class CoinAdapter(private val context: Context, private val coins: List<Coin>) : BaseAdapter() {


    override fun getCount(): Int = coins.size

    override fun getItem(position: Int): Any = coins[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutRes = if ((position + 1) % 5 == 0) {
            R.layout.coins_list_special
        } else {
            R.layout.coins_list_activity
        }

        val view = if (convertView == null || convertView.tag != layoutRes) {
            LayoutInflater.from(context).inflate(layoutRes, parent, false).apply {
                tag = layoutRes
            }
        } else {
            convertView
        }

        val coin = coins[position]

        val coinIcon = view.findViewById<ImageView>(R.id.coinIcon)
        val coinName = view.findViewById<TextView>(R.id.coinName)
        val coinSymbol = view.findViewById<TextView>(R.id.coinSymbol)

        coinName.text = coin.name

        if ((position + 1) % 5 == 0) {
            coinSymbol.text = coin.coinrankingUrl
            coinSymbol.setOnClickListener {
                val coinUrl = coin.coinrankingUrl
                if (coinUrl.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(coinUrl))
                    if (context is Activity) {
                        context.startActivity(intent)
                    } else {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.applicationContext.startActivity(intent)
                    }
                }
            }
        }
        else{
            coinSymbol.text = coin.symbol
        }

        if (coin.iconUrl.endsWith(".svg", ignoreCase = true)) {
            coinIcon.loadSvg(context, coin.iconUrl)
        } else {
            Glide.with(context)
                .applyDefaultRequestOptions(RequestOptions()
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.coin_error))
                .load(coin.iconUrl)
                .into(coinIcon)
        }

        return view
    }
    private fun ImageView.loadSvg(context: Context, url: String) {
        Thread {
            try {
                val inputStream = java.net.URL(url).openStream()
                val svg = SVG.getFromInputStream(inputStream)
                val picture: Picture = svg.renderToPicture()
                val drawable = PictureDrawable(picture)
                post {
                    setImageDrawable(drawable)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                post {
                    setImageResource(R.drawable.coin_error)
                }
            }
        }.start()
    }
}