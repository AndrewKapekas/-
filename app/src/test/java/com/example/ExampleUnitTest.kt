package com.example

import com.example.model.AppCurrency
import com.example.model.DetectedGiftItem
import com.example.model.TikTokGiftData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testTikTokGiftData_containsMajorGifts() {
    val lion = TikTokGiftData.getById("lion")
    assertNotNull(lion)
    assertEquals(29999L, lion?.coinCost)
    assertEquals(29999L, lion?.diamondValue)

    val universe = TikTokGiftData.getById("tiktok_universe")
    assertNotNull(universe)
    assertEquals(34999L, universe?.coinCost)
  }

  @Test
  fun testCurrencyPayout_calculatesCorrectValues() {
    val diamonds = 10000L
    val usdPayout = AppCurrency.USD.formatCreatorPayout(diamonds)
    assertEquals("$50.00", usdPayout)

    val buyerCostUsd = AppCurrency.USD.formatBuyerCost(1000L)
    assertEquals("$14.30", buyerCostUsd)

    val eurPayout = AppCurrency.EUR.formatCreatorPayout(diamonds)
    assertEquals("€46.00", eurPayout)
  }

  @Test
  fun testDetectedGiftItem_subtotalCalculations() {
    val galaxy = TikTokGiftData.getById("galaxy")!!
    val item = DetectedGiftItem(gift = galaxy, quantity = 3)

    assertEquals(3000L, item.subtotalCoins)
    assertEquals(3000L, item.subtotalDiamonds)
  }
}

