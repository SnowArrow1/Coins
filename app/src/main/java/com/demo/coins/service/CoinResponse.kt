package com.demo.coins.service

data class CoinResponse(
    val status: String,
    val data: CoinData
)

data class CoinData(
    val coins: List<Coin>
)

data class Coin(
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val coinrankingUrl: String
)