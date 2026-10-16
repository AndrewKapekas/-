package com.example.model

data class DetectedGiftItem(
  val gift: TikTokGift,
  val quantity: Int
) {
  val subtotalCoins: Long
    get() = gift.coinCost * quantity

  val subtotalDiamonds: Long
    get() = gift.diamondValue * quantity
}

data class ScanSession(
  val id: String,
  val title: String,
  val timestamp: Long,
  val totalCoins: Long,
  val totalDiamonds: Long,
  val items: List<DetectedGiftItem>,
  val notes: String = ""
)
