package com.example.model

import java.util.Locale

enum class AppCurrency(
  val symbol: String,
  val code: String,
  val creatorRatePerDiamond: Double, // Approx payout per diamond in currency
  val buyerRatePerCoin: Double       // Approx cost per coin in currency
) {
  EUR("€", "EUR", 0.0046, 0.0130),
  USD("$", "USD", 0.0050, 0.0143),
  GBP("£", "GBP", 0.0039, 0.0112);

  fun formatCreatorPayout(diamonds: Long): String {
    val amount = diamonds * creatorRatePerDiamond
    return String.format(Locale.US, "%s%.2f", symbol, amount)
  }

  fun formatBuyerCost(coins: Long): String {
    val amount = coins * buyerRatePerCoin
    return String.format(Locale.US, "%s%.2f", symbol, amount)
  }

  fun formatCustomAmount(amount: Double): String {
    return String.format(Locale.US, "%s%.2f", symbol, amount)
  }
}
