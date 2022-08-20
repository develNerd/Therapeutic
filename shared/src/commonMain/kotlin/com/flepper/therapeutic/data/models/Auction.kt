package com.flepper.therapeutic.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Auction(val dt: String = "",
                   val allAuctionsLotsCount: Int = 0,
                   val auctionSlug: String = "",
                   val auctionLotsCount: Int = 0,
                   val auctionName: String = "",
                   val winningBidMin: Double = 0.0,
                   val auctionTradingVolume: Int = 0,
                   val winningBidMax: Double = 0.0,
                   val winningBidMean: Double = 0.0)