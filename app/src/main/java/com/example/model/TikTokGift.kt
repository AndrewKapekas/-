package com.example.model

enum class GiftCategory(val displayName: String) {
  ALL("All"),
  CLASSIC("Classic"),
  POPULAR("Popular"),
  ELITE("Elite"),
  UNIVERSE("Legendary")
}

data class TikTokGift(
  val id: String,
  val name: String,
  val greekName: String,
  val coinCost: Long,
  val category: GiftCategory,
  val emoji: String,
  val description: String
) {
  val diamondValue: Long
    get() = coinCost // 1 Coin sent = 1 Diamond for creator
}

object TikTokGiftData {
  val ALL_GIFTS = listOf(
    TikTokGift("rose", "Rose", "Τριαντάφυλλο", 1, GiftCategory.POPULAR, "🌹", "Classic TikTok gift"),
    TikTokGift("tiktok", "TikTok", "TikTok Logo", 1, GiftCategory.CLASSIC, "🎵", "Official logo heart"),
    TikTokGift("finger_heart", "Finger Heart", "Καρδιά με Δάχτυλα", 5, GiftCategory.CLASSIC, "🫰", "Cute K-style mini heart"),
    TikTokGift("panda", "Panda", "Πάντα", 5, GiftCategory.CLASSIC, "🐼", "Cute waving panda"),
    TikTokGift("doughnut", "Doughnut", "Ντόνατ", 30, GiftCategory.POPULAR, "🍩", "Sweet tasty doughnut"),
    TikTokGift("paper_crane", "Paper Crane", "Χάρτινος Γερανός", 99, GiftCategory.POPULAR, "🕊️", "Origami peace bird"),
    TikTokGift("confetti", "Confetti", "Κονφετί", 100, GiftCategory.POPULAR, "🎉", "Party celebration blast"),
    TikTokGift("cap_and_glasses", "Cap & Glasses", "Καπέλο & Γυαλιά", 99, GiftCategory.CLASSIC, "🧢", "Cool sunglasses style"),
    TikTokGift("heart_me", "Heart Me", "Καρδούλα", 10, GiftCategory.CLASSIC, "❤️", "Show team loyalty"),
    TikTokGift("butterfly", "Butterfly", "Πεταλούδα", 169, GiftCategory.POPULAR, "🦋", "Glowing neon butterfly"),
    TikTokGift("money_gun", "Money Gun", "Όπλο Χρημάτων", 500, GiftCategory.ELITE, "🔫", "Spraying golden dollar bills"),
    TikTokGift("swan", "Swan", "Κύκνος", 699, GiftCategory.ELITE, "🦢", "Graceful glowing lake swan"),
    TikTokGift("train", "Train", "Τρένο", 899, GiftCategory.ELITE, "🚂", "Hype train rolling in"),
    TikTokGift("galaxy", "Galaxy", "Γαλαξίας", 1000, GiftCategory.ELITE, "🌌", "Cosmic swirl animation"),
    TikTokGift("whale", "Diving Whale", "Φάλαινα", 2150, GiftCategory.ELITE, "🐋", "Majestic deep blue whale"),
    TikTokGift("motorcycle", "Motorcycle", "Μοτοσυκλέτα", 2988, GiftCategory.ELITE, "🏍️", "High speed neon chopper"),
    TikTokGift("falcon", "Falcon", "Γεράκι", 10999, GiftCategory.UNIVERSE, "🦅", "Soaring golden sky hunter"),
    TikTokGift("tiktok_shuttle", "Space Shuttle", "Διαστημόπλοιο", 20000, GiftCategory.UNIVERSE, "🚀", "Rocket launch to the moon"),
    TikTokGift("dragon", "Dragon Flame", "Δράκος", 26999, GiftCategory.UNIVERSE, "🐉", "Mythic fire-breathing dragon"),
    TikTokGift("lion", "Lion", "Λιοντάρι", 29999, GiftCategory.UNIVERSE, "🦁", "The roaring king of TikTok"),
    TikTokGift("tiktok_universe", "TikTok Universe", "TikTok Universe", 34999, GiftCategory.UNIVERSE, "🪐", "Ultimate supreme TikTok gift")
  )

  fun getById(id: String): TikTokGift? = ALL_GIFTS.find { it.id == id }
}
