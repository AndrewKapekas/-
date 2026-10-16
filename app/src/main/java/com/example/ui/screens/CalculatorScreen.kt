package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppCurrency
import com.example.ui.theme.CoinGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TikTokCardBg
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import java.util.Locale

@Composable
fun CalculatorScreen(
  coinsInput: String,
  onCoinsInputChanged: (String) -> Unit,
  onPresetSelected: (Long) -> Unit,
  currency: AppCurrency,
  modifier: Modifier = Modifier
) {
  val coins = coinsInput.toLongOrNull() ?: 0L
  val diamonds = coins // 1 Coin = 1 Diamond
  val buyerCost = coins * currency.buyerRatePerCoin
  val creatorPayout = diamonds * currency.creatorRatePerDiamond
  val platformCut = buyerCost - creatorPayout

  val presets = listOf(
    Pair("70", 70L),
    Pair("350", 350L),
    Pair("700", 700L),
    Pair("1,000 (Galaxy)", 1000L),
    Pair("5,000", 5000L),
    Pair("10,000", 10000L),
    Pair("29,999 (Lion)", 29999L),
    Pair("34,999 (Universe)", 34999L),
    Pair("100,000", 100000L)
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
          brush = Brush.horizontalGradient(listOf(TikTokCyan.copy(alpha = 0.5f), CoinGold.copy(alpha = 0.5f)))
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(TikTokCyan.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                tint = TikTokCyan,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.calc_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = stringResource(R.string.calc_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA5A6AF)
              )
            }
          }
        }
      }
    }

    // Input Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B26))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = stringResource(R.string.input_coins_label),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = coinsInput,
            onValueChange = onCoinsInputChanged,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_coins_field"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = CoinGold
              )
            },
            trailingIcon = {
              Text(
                text = "COINS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF78798C),
                modifier = Modifier.padding(end = 12.dp)
              )
            },
            placeholder = { Text("e.g. 10000", color = Color(0xFF555566)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = TikTokPink,
              unfocusedBorderColor = Color(0xFF333344),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedContainerColor = Color(0xFF14141E),
              unfocusedContainerColor = Color(0xFF14141E)
            ),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Presets row
          Text(
            text = stringResource(R.string.preset_coins_label),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF888899)
          )

          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(presets) { (label, amount) ->
              val isSelected = coins == amount
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) TikTokPink else Color(0xFF252535))
                  .border(
                    1.dp,
                    if (isSelected) TikTokPink else Color(0xFF38384A),
                    RoundedCornerShape(12.dp)
                  )
                  .clickable { onPresetSelected(amount) }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
                  .testTag("preset_$amount")
              ) {
                Text(
                  text = label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else Color(0xFFC0C1CD)
                )
              }
            }
          }
        }
      }
    }

    // Results Breakdown
    item {
      Text(
        text = stringResource(R.string.payout_breakdown),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )
    }

    // Creator Payout Card (Highlight)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF162520)),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(SuccessGreen, TikTokCyan))
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
                .size(46.dp)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Savings,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.creator_cut),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = String.format(Locale.US, "%,d Diamonds credited", diamonds),
                fontSize = 11.sp,
                color = DiamondCyan
              )
            }
          }

          Text(
            text = currency.formatCreatorPayout(diamonds),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
            color = SuccessGreen
          )
        }
      }
    }

    // Sender Cost Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF221B22)),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(TikTokPink.copy(alpha = 0.5f), Color(0xFF332233)))
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
                .size(44.dp)
                .clip(CircleShape)
                .background(TikTokPink.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = TikTokPink,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.sender_paid),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "In-app coin purchase cost",
                fontSize = 11.sp,
                color = Color(0xFF90919F)
              )
            }
          }

          Text(
            text = currency.formatBuyerCost(coins),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
        }
      }
    }

    // Platform Commission Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A22))
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
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF2B2B38)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Percent,
                contentDescription = null,
                tint = Color(0xFF9EA0B0),
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.tiktok_share),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFFD0D0DC)
              )
              Text(
                text = "Processing & platform revenue",
                fontSize = 11.sp,
                color = Color(0xFF787988)
              )
            }
          }

          Text(
            text = currency.formatCustomAmount(platformCut.coerceAtLeast(0.0)),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFFB0B0BF)
          )
        }
      }
    }

    // Rule Info Box
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF141A24))
          .border(1.dp, Color(0xFF253344), RoundedCornerShape(12.dp))
          .padding(14.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = TikTokCyan,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = stringResource(R.string.calc_rule_info),
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFFA5B4C8),
          lineHeight = 18.sp
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
