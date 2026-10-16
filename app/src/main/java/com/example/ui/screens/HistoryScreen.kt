package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppCurrency
import com.example.model.ScanSession
import com.example.ui.theme.CoinGold
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TikTokCardBg
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
  sessions: List<ScanSession>,
  currency: AppCurrency,
  onDeleteSession: (String) -> Unit,
  onClearAll: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showClearConfirmDialog by remember { mutableStateOf(false) }
  val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

  if (showClearConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showClearConfirmDialog = false },
      title = { Text(text = stringResource(R.string.btn_clear_history), color = Color.White) },
      text = { Text(text = stringResource(R.string.confirm_clear), color = Color(0xFFC0C1CD)) },
      confirmButton = {
        Button(
          onClick = {
            onClearAll()
            showClearConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = TikTokPink)
        ) {
          Text(text = stringResource(R.string.btn_confirm))
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showClearConfirmDialog = false }) {
          Text(text = stringResource(R.string.btn_cancel), color = Color(0xFFA5A6AF))
        }
      },
      containerColor = Color(0xFF1E1E28)
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TikTokCardBg),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(TikTokPink.copy(alpha = 0.4f), TikTokCyan.copy(alpha = 0.4f)))
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(TikTokPink.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = TikTokPink,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.history_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "${sessions.size} recorded sessions",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA5A6AF)
              )
            }
          }

          if (sessions.isNotEmpty()) {
            OutlinedButton(
              onClick = { showClearConfirmDialog = true },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6B6B)),
              modifier = Modifier.testTag("btn_clear_history")
            ) {
              Text(
                text = "Clear",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    if (sessions.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF161622))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = null,
              tint = Color(0xFF4B4C5E),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = stringResource(R.string.history_empty),
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF888899),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    } else {
      items(sessions, key = { it.id }) { session ->
        HistorySessionCard(
          session = session,
          currency = currency,
          formattedDate = dateFormat.format(Date(session.timestamp)),
          onDelete = { onDeleteSession(session.id) }
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun HistorySessionCard(
  session: ScanSession,
  currency: AppCurrency,
  formattedDate: String,
  onDelete: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C28)),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(Color(0xFF2C2C3E), Color(0xFF1C1C28)))
    )
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = session.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
          Text(
            text = formattedDate,
            fontSize = 11.sp,
            color = Color(0xFF8A8B9A)
          )
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier.testTag("delete_session_${session.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete session",
            tint = Color(0xFF8A8B9A),
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Totals banner
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF252535))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.MonetizationOn,
            contentDescription = null,
            tint = CoinGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = String.format(Locale.US, "%,d Coins", session.totalCoins),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CoinGold
          )
        }

        Text(
          text = currency.formatCreatorPayout(session.totalDiamonds),
          fontSize = 12.sp,
          fontWeight = FontWeight.Black,
          color = SuccessGreen
        )
      }

      // Gift breakdown pills
      if (session.items.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val previewItems = session.items.take(4)
          previewItems.forEach { item ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF2D2D3D))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${item.gift.emoji} x${item.quantity}",
                fontSize = 10.sp,
                color = Color.White
              )
            }
          }
          if (session.items.size > 4) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF2D2D3D))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "+${session.items.size - 4} more",
                fontSize = 10.sp,
                color = Color(0xFFA5A6B4)
              )
            }
          }
        }
      }
    }
  }
}
