package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppCurrency
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink

@Composable
fun TokTopBar(
  currentCurrency: AppCurrency,
  onCurrencySelected: (AppCurrency) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF0A0A0C),
            Color(0xFF141418)
          )
        )
      )
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // App Brand Identity
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                colors = listOf(TikTokPink, TikTokCyan)
              )
            )
            .padding(2.dp),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0xFF121212)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.MonetizationOn,
              contentDescription = "TokCoin Logo",
              tint = CoinGold,
              modifier = Modifier.size(22.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "TOK",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
              ),
              color = TikTokPink
            )
            Text(
              text = "COIN",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
              ),
              color = TikTokCyan
            )
            Text(
              text = " SCANNER",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              ),
              color = Color.White
            )
          }
          Text(
            text = "TikTok Live Gift & Coin Detector",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF9E9EA7)
          )
        }
      }

      // Currency Switcher Selector
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFF202028))
          .border(1.dp, Color(0xFF33333F), RoundedCornerShape(20.dp))
          .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        AppCurrency.entries.forEach { currency ->
          val isSelected = currency == currentCurrency
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(
                if (isSelected) TikTokPink else Color.Transparent
              )
              .clickable { onCurrencySelected(currency) }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("currency_${currency.code.lowercase()}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = currency.symbol,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              ),
              color = if (isSelected) Color.White else Color(0xFF9E9EA7)
            )
          }
        }
      }
    }
  }
}
