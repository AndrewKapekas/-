package com.example.data

import android.content.Context
import android.net.Uri
import com.example.model.DetectedGiftItem
import com.example.model.GiftCategory
import com.example.model.ScanSession
import com.example.model.TikTokGift
import com.example.model.TikTokGiftData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class TokCoinRepository(private val context: Context) {

  private val prefs = context.getSharedPreferences("tokcoin_prefs", Context.MODE_PRIVATE)
  private val _sessionsFlow = MutableStateFlow<List<ScanSession>>(emptyList())
  val sessionsFlow: StateFlow<List<ScanSession>> = _sessionsFlow.asStateFlow()

  init {
    loadSessions()
  }

  fun getGifts(category: GiftCategory = GiftCategory.ALL): List<TikTokGift> {
    return if (category == GiftCategory.ALL) {
      TikTokGiftData.ALL_GIFTS
    } else {
      TikTokGiftData.ALL_GIFTS.filter { it.category == category }
    }
  }

  suspend fun scanImage(imageUri: Uri?): List<DetectedGiftItem> {
    // Simulate real-time computer vision / OCR scanning pass on stream screenshot
    delay(1400)

    // Generate smart detection results based on popular stream gift combinations
    val detected = mutableListOf<DetectedGiftItem>()
    val gifts = TikTokGiftData.ALL_GIFTS

    // Realistic stream frame detection
    val sampleSelections = listOf(
      Pair("lion", 1),
      Pair("galaxy", 2),
      Pair("money_gun", 3),
      Pair("rose", 45),
      Pair("doughnut", 8),
      Pair("confetti", 12)
    )

    for ((id, count) in sampleSelections) {
      gifts.find { it.id == id }?.let { gift ->
        detected.add(DetectedGiftItem(gift = gift, quantity = count))
      }
    }

    return detected
  }

  suspend fun generateSampleScan(scenarioIndex: Int = 0): List<DetectedGiftItem> {
    delay(800)
    val detected = mutableListOf<DetectedGiftItem>()
    val gifts = TikTokGiftData.ALL_GIFTS

    val scenarios = listOf(
      listOf(Pair("tiktok_universe", 1), Pair("lion", 1), Pair("rose", 99)),
      listOf(Pair("dragon", 1), Pair("falcon", 1), Pair("money_gun", 4), Pair("rose", 50)),
      listOf(Pair("galaxy", 5), Pair("swan", 2), Pair("train", 3), Pair("doughnut", 15)),
      listOf(Pair("tiktok_shuttle", 1), Pair("whale", 2), Pair("confetti", 20), Pair("heart_me", 60))
    )

    val chosen = scenarios[scenarioIndex % scenarios.size]
    for ((id, count) in chosen) {
      gifts.find { it.id == id }?.let { gift ->
        detected.add(DetectedGiftItem(gift = gift, quantity = count))
      }
    }
    return detected
  }

  fun saveSession(session: ScanSession) {
    val current = _sessionsFlow.value.toMutableList()
    current.add(0, session)
    _sessionsFlow.value = current
    persistSessions(current)
  }

  fun deleteSession(sessionId: String) {
    val updated = _sessionsFlow.value.filter { it.id != sessionId }
    _sessionsFlow.value = updated
    persistSessions(updated)
  }

  fun clearAllSessions() {
    _sessionsFlow.value = emptyList()
    prefs.edit().remove("saved_sessions_json").apply()
  }

  private fun persistSessions(list: List<ScanSession>) {
    try {
      val jsonArray = JSONArray()
      for (session in list) {
        val obj = JSONObject()
        obj.put("id", session.id)
        obj.put("title", session.title)
        obj.put("timestamp", session.timestamp)
        obj.put("totalCoins", session.totalCoins)
        obj.put("totalDiamonds", session.totalDiamonds)
        obj.put("notes", session.notes)

        val itemsArray = JSONArray()
        for (item in session.items) {
          val itemObj = JSONObject()
          itemObj.put("giftId", item.gift.id)
          itemObj.put("quantity", item.quantity)
          itemsArray.put(itemObj)
        }
        obj.put("items", itemsArray)
        jsonArray.put(obj)
      }
      prefs.edit().putString("saved_sessions_json", jsonArray.toString()).apply()
    } catch (_: Exception) {}
  }

  private fun loadSessions() {
    val jsonStr = prefs.getString("saved_sessions_json", null) ?: return
    try {
      val jsonArray = JSONArray(jsonStr)
      val list = mutableListOf<ScanSession>()
      for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        val id = obj.optString("id", UUID.randomUUID().toString())
        val title = obj.optString("title", "Live Stream Scan")
        val timestamp = obj.optLong("timestamp", System.currentTimeMillis())
        val totalCoins = obj.optLong("totalCoins", 0L)
        val totalDiamonds = obj.optLong("totalDiamonds", totalCoins)
        val notes = obj.optString("notes", "")

        val itemsArray = obj.optJSONArray("items")
        val itemsList = mutableListOf<DetectedGiftItem>()
        if (itemsArray != null) {
          for (j in 0 until itemsArray.length()) {
            val itemObj = itemsArray.getJSONObject(j)
            val giftId = itemObj.optString("giftId", "")
            val qty = itemObj.optInt("quantity", 1)
            TikTokGiftData.getById(giftId)?.let { gift ->
              itemsList.add(DetectedGiftItem(gift = gift, quantity = qty))
            }
          }
        }

        list.add(
          ScanSession(
            id = id,
            title = title,
            timestamp = timestamp,
            totalCoins = totalCoins,
            totalDiamonds = totalDiamonds,
            items = itemsList,
            notes = notes
          )
        )
      }
      _sessionsFlow.value = list
    } catch (_: Exception) {}
  }
}
