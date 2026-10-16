package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TokCoinRepository
import com.example.model.AppCurrency
import com.example.model.DetectedGiftItem
import com.example.model.GiftCategory
import com.example.model.ScanSession
import com.example.model.TikTokGift
import com.example.model.TikTokGiftData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppTab {
  SCANNER,
  GIFTS,
  CALCULATOR,
  CHEST,
  HISTORY
}

data class ScannerUiState(
  val imageUri: Uri? = null,
  val isScanning: Boolean = false,
  val detectedItems: List<DetectedGiftItem> = emptyList(),
  val infoMessage: String? = null
) {
  val totalCoins: Long
    get() = detectedItems.sumOf { it.subtotalCoins }

  val totalDiamonds: Long
    get() = detectedItems.sumOf { it.subtotalDiamonds }
}

data class ChestUiState(
  val coinsPool: Long = 1000,
  val viewersCount: Long = 85,
  val initialSeconds: Int = 180,
  val remainingSeconds: Int = 180,
  val isRunning: Boolean = false,
  val isCompleted: Boolean = false
) {
  val progress: Float
    get() = if (initialSeconds > 0) remainingSeconds.toFloat() / initialSeconds else 0f

  val averageWinPerViewer: Double
    get() = if (viewersCount > 0) coinsPool.toDouble() / viewersCount else 0.0
}

class TokCoinViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = TokCoinRepository(application.applicationContext)

  // Navigation & Preferences
  private val _currentTab = MutableStateFlow(AppTab.SCANNER)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  private val _currency = MutableStateFlow(AppCurrency.EUR)
  val currency: StateFlow<AppCurrency> = _currency.asStateFlow()

  // Scanner Tab State
  private val _scannerState = MutableStateFlow(ScannerUiState())
  val scannerState: StateFlow<ScannerUiState> = _scannerState.asStateFlow()

  // Gift Catalog / Live Tally State
  private val _selectedGiftCategory = MutableStateFlow(GiftCategory.ALL)
  val selectedGiftCategory: StateFlow<GiftCategory> = _selectedGiftCategory.asStateFlow()

  private val _giftTally = MutableStateFlow<Map<String, Int>>(emptyMap())
  val giftTally: StateFlow<Map<String, Int>> = _giftTally.asStateFlow()

  // Calculator State
  private val _calcCoinsInput = MutableStateFlow("10000")
  val calcCoinsInput: StateFlow<String> = _calcCoinsInput.asStateFlow()

  // Chest Drop State
  private val _chestState = MutableStateFlow(ChestUiState())
  val chestState: StateFlow<ChestUiState> = _chestState.asStateFlow()
  private var timerJob: Job? = null

  // Saved Sessions from Repository
  val savedSessions: StateFlow<List<ScanSession>> = repository.sessionsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun selectTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun setCurrency(newCurrency: AppCurrency) {
    _currency.value = newCurrency
  }

  // Scanner methods
  fun onImageSelected(uri: Uri?) {
    _scannerState.value = _scannerState.value.copy(
      imageUri = uri,
      isScanning = true,
      infoMessage = null
    )
    vibrate(50)

    viewModelScope.launch {
      val results = repository.scanImage(uri)
      _scannerState.value = _scannerState.value.copy(
        isScanning = false,
        detectedItems = results,
        infoMessage = "Found ${results.size} gift types in screenshot!"
      )
      vibrate(100)
    }
  }

  fun scanSampleStream(index: Int = 0) {
    _scannerState.value = _scannerState.value.copy(
      imageUri = null,
      isScanning = true,
      infoMessage = null
    )
    vibrate(40)

    viewModelScope.launch {
      val results = repository.generateSampleScan(index)
      _scannerState.value = _scannerState.value.copy(
        isScanning = false,
        detectedItems = results,
        infoMessage = "Simulated Live Stream frame scanned successfully!"
      )
      vibrate(80)
    }
  }

  fun adjustDetectedGiftQuantity(giftId: String, delta: Int) {
    val current = _scannerState.value.detectedItems.toMutableList()
    val index = current.indexOfFirst { it.gift.id == giftId }
    if (index >= 0) {
      val item = current[index]
      val newQty = (item.quantity + delta).coerceAtLeast(0)
      if (newQty == 0) {
        current.removeAt(index)
      } else {
        current[index] = item.copy(quantity = newQty)
      }
      _scannerState.value = _scannerState.value.copy(detectedItems = current)
      vibrate(25)
    }
  }

  fun addDetectedGift(gift: TikTokGift) {
    val current = _scannerState.value.detectedItems.toMutableList()
    val index = current.indexOfFirst { it.gift.id == gift.id }
    if (index >= 0) {
      current[index] = current[index].copy(quantity = current[index].quantity + 1)
    } else {
      current.add(0, DetectedGiftItem(gift = gift, quantity = 1))
    }
    _scannerState.value = _scannerState.value.copy(detectedItems = current)
    vibrate(30)
  }

  fun saveCurrentScanSession(customTitle: String = "") {
    val items = _scannerState.value.detectedItems
    if (items.isEmpty()) return

    val totalCoins = items.sumOf { it.subtotalCoins }
    val totalDiamonds = items.sumOf { it.subtotalDiamonds }
    val title = if (customTitle.isNotBlank()) customTitle else "Live Stream Scan #${savedSessions.value.size + 1}"

    val session = ScanSession(
      id = UUID.randomUUID().toString(),
      title = title,
      timestamp = System.currentTimeMillis(),
      totalCoins = totalCoins,
      totalDiamonds = totalDiamonds,
      items = items
    )
    repository.saveSession(session)
    _scannerState.value = _scannerState.value.copy(infoMessage = "Session saved to history!")
    vibrate(120)
  }

  // Gift Tally methods
  fun setGiftCategory(category: GiftCategory) {
    _selectedGiftCategory.value = category
  }

  fun incrementGiftTally(giftId: String, delta: Int = 1) {
    val current = _giftTally.value.toMutableMap()
    val newCount = (current.getOrDefault(giftId, 0) + delta).coerceAtLeast(0)
    if (newCount == 0) {
      current.remove(giftId)
    } else {
      current[giftId] = newCount
    }
    _giftTally.value = current
    vibrate(30)
  }

  fun resetGiftTally() {
    _giftTally.value = emptyMap()
    vibrate(50)
  }

  fun getTallyTotalCoins(): Long {
    var total = 0L
    for ((giftId, count) in _giftTally.value) {
      TikTokGiftData.getById(giftId)?.let {
        total += it.coinCost * count
      }
    }
    return total
  }

  // Calculator methods
  fun onCalcCoinsChanged(input: String) {
    val sanitized = input.filter { it.isDigit() }
    _calcCoinsInput.value = sanitized
  }

  fun setPresetCoins(amount: Long) {
    _calcCoinsInput.value = amount.toString()
    vibrate(25)
  }

  // Chest drop timer methods
  fun setChestCoins(coins: Long) {
    _chestState.value = _chestState.value.copy(coinsPool = coins.coerceAtLeast(1))
  }

  fun setChestViewers(viewers: Long) {
    _chestState.value = _chestState.value.copy(viewersCount = viewers.coerceAtLeast(1))
  }

  fun setChestDuration(seconds: Int) {
    _chestState.value = _chestState.value.copy(
      initialSeconds = seconds,
      remainingSeconds = seconds,
      isRunning = false,
      isCompleted = false
    )
    timerJob?.cancel()
  }

  fun startChestTimer() {
    if (_chestState.value.isRunning) return
    val currentRemaining = if (_chestState.value.isCompleted || _chestState.value.remainingSeconds <= 0) {
      _chestState.value.initialSeconds
    } else {
      _chestState.value.remainingSeconds
    }

    _chestState.value = _chestState.value.copy(
      isRunning = true,
      isCompleted = false,
      remainingSeconds = currentRemaining
    )

    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_chestState.value.remainingSeconds > 0 && _chestState.value.isRunning) {
        delay(1000)
        val nextSec = _chestState.value.remainingSeconds - 1
        _chestState.value = _chestState.value.copy(
          remainingSeconds = nextSec,
          isCompleted = nextSec == 0,
          isRunning = nextSec > 0
        )
      }
      if (_chestState.value.isCompleted) {
        vibrate(500)
      }
    }
  }

  fun pauseChestTimer() {
    timerJob?.cancel()
    _chestState.value = _chestState.value.copy(isRunning = false)
  }

  fun resetChestTimer() {
    timerJob?.cancel()
    _chestState.value = _chestState.value.copy(
      isRunning = false,
      isCompleted = false,
      remainingSeconds = _chestState.value.initialSeconds
    )
    vibrate(40)
  }

  // History methods
  fun deleteSession(id: String) {
    repository.deleteSession(id)
    vibrate(30)
  }

  fun clearAllHistory() {
    repository.clearAllSessions()
    vibrate(100)
  }

  private fun vibrate(durationMillis: Long) {
    try {
      val ctx = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator?.vibrate(
            VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
          )
        } else {
          @Suppress("DEPRECATION")
          vibrator?.vibrate(durationMillis)
        }
      }
    } catch (_: Exception) {}
  }

  override fun onCleared() {
    super.onCleared()
    timerJob?.cancel()
  }
}
