package com.demo.coins.service

data class CoinResponse(
    val status: String,
    val data: CoinData
)

data class CoinData(
    val coins: List<Coin>
)

data class Coin(
    val uuid: String,
    val name: String,
    val symbol: String,
    val price: String,
    val iconUrl: String
)