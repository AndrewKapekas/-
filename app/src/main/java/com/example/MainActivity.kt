package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TokBottomNav
import com.example.ui.components.TokTopBar
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.ChestTimerScreen
import com.example.ui.screens.GiftCatalogScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TikTokDarkBg
import com.example.viewmodel.AppTab
import com.example.viewmodel.TokCoinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        TokCoinApp()
      }
    }
  }
}

@Composable
fun TokCoinApp(viewModel: TokCoinViewModel = viewModel()) {
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val currency by viewModel.currency.collectAsStateWithLifecycle()
  val scannerState by viewModel.scannerState.collectAsStateWithLifecycle()
  val selectedGiftCategory by viewModel.selectedGiftCategory.collectAsStateWithLifecycle()
  val giftTally by viewModel.giftTally.collectAsStateWithLifecycle()
  val calcCoinsInput by viewModel.calcCoinsInput.collectAsStateWithLifecycle()
  val chestState by viewModel.chestState.collectAsStateWithLifecycle()
  val savedSessions by viewModel.savedSessions.collectAsStateWithLifecycle()

  // Back handling: If on any secondary tab, pressing Back returns to the primary Scanner tab
  BackHandler(enabled = currentTab != AppTab.SCANNER) {
    viewModel.selectTab(AppTab.SCANNER)
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(TikTokDarkBg),
    topBar = {
      TokTopBar(
        currentCurrency = currency,
        onCurrencySelected = { viewModel.setCurrency(it) }
      )
    },
    bottomBar = {
      TokBottomNav(
        currentTab = currentTab,
        onTabSelected = { viewModel.selectTab(it) }
      )
    },
    containerColor = TikTokDarkBg
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      Crossfade(targetState = currentTab, label = "tab_transition") { tab ->
        when (tab) {
          AppTab.SCANNER -> {
            ScannerScreen(
              scannerState = scannerState,
              currency = currency,
              onImageSelected = { viewModel.onImageSelected(it) },
              onSampleScan = { viewModel.scanSampleStream(it) },
              onAdjustQuantity = { id, delta -> viewModel.adjustDetectedGiftQuantity(id, delta) },
              onAddGift = { viewModel.addDetectedGift(it) },
              onSaveSession = { viewModel.saveCurrentScanSession() }
            )
          }

          AppTab.GIFTS -> {
            GiftCatalogScreen(
              selectedCategory = selectedGiftCategory,
              onCategorySelected = { viewModel.setGiftCategory(it) },
              giftTally = giftTally,
              onIncrementGift = { id, delta -> viewModel.incrementGiftTally(id, delta) },
              onResetTally = { viewModel.resetGiftTally() },
              totalCoins = viewModel.getTallyTotalCoins(),
              currency = currency
            )
          }

          AppTab.CALCULATOR -> {
            CalculatorScreen(
              coinsInput = calcCoinsInput,
              onCoinsInputChanged = { viewModel.onCalcCoinsChanged(it) },
              onPresetSelected = { viewModel.setPresetCoins(it) },
              currency = currency
            )
          }

          AppTab.CHEST -> {
            ChestTimerScreen(
              chestState = chestState,
              onStartTimer = { viewModel.startChestTimer() },
              onPauseTimer = { viewModel.pauseChestTimer() },
              onResetTimer = { viewModel.resetChestTimer() },
              onSetDuration = { viewModel.setChestDuration(it) },
              onSetCoins = { viewModel.setChestCoins(it) },
              onSetViewers = { viewModel.setChestViewers(it) }
            )
          }

          AppTab.HISTORY -> {
            HistoryScreen(
              sessions = savedSessions,
              currency = currency,
              onDeleteSession = { viewModel.deleteSession(it) },
              onClearAll = { viewModel.clearAllHistory() }
            )
          }
        }
      }
    }
  }
}
