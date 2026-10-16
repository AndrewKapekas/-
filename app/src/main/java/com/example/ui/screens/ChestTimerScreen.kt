package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CoinGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TikTokCardBg
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import com.example.viewmodel.ChestUiState
import java.util.Locale

@Composable
fun ChestTimerScreen(
  chestState: ChestUiState,
  onStartTimer: () -> Unit,
  onPauseTimer: () -> Unit,
  onResetTimer: () -> Unit,
  onSetDuration: (Int) -> Unit,
  onSetCoins: (Long) -> Unit,
  onSetViewers: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val minutes = chestState.remainingSeconds / 60
  val seconds = chestState.remainingSeconds % 60
  val timeFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)

  val coinPoolPresets = listOf(100L, 500L, 1000L, 2000L, 5000L, 10000L)
  val durationPresets = listOf(
    Pair("1 Min", 60),
    Pair("3 Mins", 180),
    Pair("5 Mins", 300)
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TikTokCardBg),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(CoinGold.copy(alpha = 0.5f), TikTokPink.copy(alpha = 0.5f)))
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CoinGold.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = CoinGold,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.chest_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = stringResource(R.string.chest_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA5A6AF)
              )
            }
          }
        }
      }
    }

    // Dropping alert when finished
    item {
      AnimatedVisibility(
        visible = chestState.isCompleted,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1018)),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(TikTokPink, CoinGold))
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = TikTokPink,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = stringResource(R.string.chest_alert_ready),
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
          }
        }
      }
    }

    // Circular Countdown Timer Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161622)),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.verticalGradient(listOf(Color(0xFF2C2C3E), Color(0xFF161622)))
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Circular Progress Indicator Container
          Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
          ) {
            // Background track
            CircularProgressIndicator(
              progress = { 1f },
              modifier = Modifier.fillMaxSize(),
              color = Color(0xFF222232),
              strokeWidth = 10.dp,
              strokeCap = StrokeCap.Round
            )

            // Dynamic progress
            CircularProgressIndicator(
              progress = { chestState.progress },
              modifier = Modifier.fillMaxSize(),
              color = if (chestState.remainingSeconds <= 10) TikTokPink else CoinGold,
              strokeWidth = 10.dp,
              strokeCap = StrokeCap.Round
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = timeFormatted,
                style = MaterialTheme.typography.displayMedium.copy(
                  fontWeight = FontWeight.Black,
                  letterSpacing = 1.sp
                ),
                color = Color.White
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (chestState.isRunning) "DROP COUNTDOWN" else if (chestState.isCompleted) "DROP READY!" else "PAUSED",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = if (chestState.isRunning) TikTokCyan else if (chestState.isCompleted) TikTokPink else Color(0xFF7E7F8C)
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Control Buttons (Start / Pause / Reset)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            if (!chestState.isRunning) {
              Button(
                onClick = onStartTimer,
                modifier = Modifier
                  .weight(1f)
                  .testTag("btn_start_chest_timer"),
                colors = ButtonDefaults.buttonColors(containerColor = TikTokPink),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = stringResource(R.string.btn_start_chest_timer),
                  fontWeight = FontWeight.Bold
                )
              }
            } else {
              Button(
                onClick = onPauseTimer,
                modifier = Modifier
                  .weight(1f)
                  .testTag("btn_pause_chest_timer"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A4A5A)),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(imageVector = Icons.Default.Pause, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Pause", fontWeight = FontWeight.Bold)
              }
            }

            OutlinedButton(
              onClick = onResetTimer,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFA0A0AF)),
              modifier = Modifier.testTag("btn_reset_chest_timer")
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = stringResource(R.string.btn_reset_chest_timer))
            }
          }
        }
      }
    }

    // Chest Settings (Coins Pool & Duration Presets)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF191925))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = stringResource(R.string.chest_total_coins),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )

          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(coinPoolPresets) { coins ->
              val isSelected = chestState.coinsPool == coins
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) CoinGold else Color(0xFF242435))
                  .border(1.dp, if (isSelected) CoinGold else Color(0xFF38384E), RoundedCornerShape(10.dp))
                  .clickable { onSetCoins(coins) }
                  .padding(horizontal = 12.dp, vertical = 6.dp)
                  .testTag("chest_coins_$coins")
              ) {
                Text(
                  text = "$coins Coins",
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.Black else Color.White
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = stringResource(R.string.chest_timer_label),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            durationPresets.forEach { (label, secs) ->
              val isSelected = chestState.initialSeconds == secs
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) TikTokCyan else Color(0xFF242435))
                  .border(1.dp, if (isSelected) TikTokCyan else Color(0xFF38384E), RoundedCornerShape(10.dp))
                  .clickable { onSetDuration(secs) }
                  .padding(vertical = 8.dp)
                  .testTag("chest_dur_$secs"),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.Black else Color.White
                )
              }
            }
          }
        }
      }
    }

    // Odds & Competition Analytics
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161C24)),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(TikTokCyan.copy(alpha = 0.4f), Color(0xFF161C24)))
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Chest Odds & Probability",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1F2833))
                .padding(12.dp)
            ) {
              Column {
                Text(
                  text = stringResource(R.string.chest_stat_avg_win),
                  fontSize = 10.sp,
                  color = Color(0xFF90A4AE)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = String.format(Locale.US, "%.1f Coins", chestState.averageWinPerViewer),
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                  color = CoinGold
                )
              }
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1F2833))
                .padding(12.dp)
            ) {
              Column {
                Text(
                  text = "Estimated Viewers",
                  fontSize = 10.sp,
                  color = Color(0xFF90A4AE)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = TikTokCyan,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${chestState.viewersCount}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
