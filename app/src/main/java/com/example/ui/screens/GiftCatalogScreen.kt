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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppCurrency
import com.example.model.GiftCategory
import com.example.model.TikTokGift
import com.example.model.TikTokGiftData
import com.example.ui.theme.CoinGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TikTokCardBg
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import java.util.Locale

@Composable
fun GiftCatalogScreen(
  selectedCategory: GiftCategory,
  onCategorySelected: (GiftCategory) -> Unit,
  giftTally: Map<String, Int>,
  onIncrementGift: (String, Int) -> Unit,
  onResetTally: () -> Unit,
  totalCoins: Long,
  currency: AppCurrency,
  modifier: Modifier = Modifier
) {
  val gifts = if (selectedCategory == GiftCategory.ALL) {
    TikTokGiftData.ALL_GIFTS
  } else {
    TikTokGiftData.ALL_GIFTS.filter { it.category == selectedCategory }
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
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(TikTokPink.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = TikTokPink,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.gift_catalog_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = stringResource(R.string.gift_catalog_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA5A6AF)
              )
            }
          }
        }
      }
    }

    // Live Tally Banner Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161620)),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(CoinGold, TikTokCyan))
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = stringResource(R.string.tally_summary_coins, totalCoins),
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
              color = CoinGold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = stringResource(
                R.string.tally_summary_cash,
                currency.formatCreatorPayout(totalCoins)
              ),
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
              color = SuccessGreen
            )
          }

          if (totalCoins > 0) {
            OutlinedButton(
              onClick = onResetTally,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6B6B)),
              modifier = Modifier.testTag("btn_reset_tally")
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = stringResource(R.string.btn_reset_tally),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Category Filter Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(GiftCategory.entries) { category ->
          val isSelected = category == selectedCategory
          FilterChip(
            selected = isSelected,
            onClick = { onCategorySelected(category) },
            label = {
              Text(
                text = when (category) {
                  GiftCategory.ALL -> stringResource(R.string.filter_all)
                  GiftCategory.CLASSIC -> "Classic"
                  GiftCategory.POPULAR -> stringResource(R.string.filter_popular)
                  GiftCategory.ELITE -> stringResource(R.string.filter_elite)
                  GiftCategory.UNIVERSE -> stringResource(R.string.filter_universe)
                },
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = TikTokPink,
              selectedLabelColor = Color.White,
              containerColor = Color(0xFF22222E),
              labelColor = Color(0xFFA5A6B4)
            ),
            modifier = Modifier.testTag("filter_${category.name.lowercase()}")
          )
        }
      }
    }

    // List of Gifts
    items(gifts, key = { it.id }) { gift ->
      val count = giftTally.getOrDefault(gift.id, 0)
      GiftCatalogRow(
        gift = gift,
        count = count,
        currency = currency,
        onIncrement = { delta -> onIncrementGift(gift.id, delta) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun GiftCatalogRow(
  gift: TikTokGift,
  count: Int,
  currency: AppCurrency,
  onIncrement: (Int) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1D1D27)),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        if (count > 0) listOf(TikTokPink.copy(alpha = 0.6f), CoinGold.copy(alpha = 0.6f))
        else listOf(Color(0xFF2E2E3E), Color(0xFF1D1D27))
      )
    )
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Icon Box
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF282838)),
          contentAlignment = Alignment.Center
        ) {
          Text(text = gift.emoji, fontSize = 28.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Titles and cost
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = gift.name,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = gift.greekName,
              fontSize = 11.sp,
              color = Color(0xFF90919D)
            )
          }

          Spacer(modifier = Modifier.height(3.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.MonetizationOn,
              contentDescription = null,
              tint = CoinGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = String.format(Locale.US, "%,d Coins", gift.coinCost),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = CoinGold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "•",
              fontSize = 11.sp,
              color = Color(0xFF555566)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = currency.formatCreatorPayout(gift.diamondValue),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = SuccessGreen
            )
          }
        }

        // Current count indicator
        if (count > 0) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(TikTokPink)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "x$count",
              fontSize = 12.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Quick tap buttons: -1, +1, +5, +10
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (count > 0) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF2B2B3A))
              .clickable { onIncrement(-1) }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "-1", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(TikTokPink.copy(alpha = 0.2f))
            .border(1.dp, TikTokPink.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable { onIncrement(1) }
            .padding(vertical = 8.dp)
            .testTag("btn_tap_plus1_${gift.id}"),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "+1", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TikTokPink)
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2A2B3B))
            .clickable { onIncrement(5) }
            .padding(vertical = 8.dp)
            .testTag("btn_tap_plus5_${gift.id}"),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "+5", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TikTokCyan)
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2A2B3B))
            .clickable { onIncrement(10) }
            .padding(vertical = 8.dp)
            .testTag("btn_tap_plus10_${gift.id}"),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "+10", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CoinGold)
        }
      }
    }
  }
}
