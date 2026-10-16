package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.AppCurrency
import com.example.model.DetectedGiftItem
import com.example.model.TikTokGift
import com.example.model.TikTokGiftData
import com.example.ui.theme.CoinGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TikTokCardBg
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import com.example.viewmodel.ScannerUiState
import java.util.Locale

@Composable
fun ScannerScreen(
  scannerState: ScannerUiState,
  currency: AppCurrency,
  onImageSelected: (Uri?) -> Unit,
  onSampleScan: (Int) -> Unit,
  onAdjustQuantity: (String, Int) -> Unit,
  onAddGift: (TikTokGift) -> Unit,
  onSaveSession: () -> Unit,
  modifier: Modifier = Modifier
) {
  var sampleCounter by remember { mutableIntStateOf(0) }

  // Zero-permission Android Photo Picker contract
  val pickMediaLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      onImageSelected(uri)
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
  val scanLaserOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "laser_pos"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header section
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TikTokCardBg),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(TikTokPink.copy(alpha = 0.5f), TikTokCyan.copy(alpha = 0.5f)))
        )
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(TikTokPink.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Science,
                contentDescription = null,
                tint = TikTokPink,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = stringResource(R.string.scanner_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = stringResource(R.string.scanner_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA5A6AF)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Action buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = {
                pickMediaLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              },
              modifier = Modifier
                .weight(1f)
                .testTag("btn_select_screenshot"),
              colors = ButtonDefaults.buttonColors(containerColor = TikTokPink),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = stringResource(R.string.btn_pick_screenshot),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            OutlinedButton(
              onClick = {
                sampleCounter++
                onSampleScan(sampleCounter)
              },
              modifier = Modifier
                .weight(1f)
                .testTag("btn_sample_scan"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = TikTokCyan)
            ) {
              Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = TikTokCyan
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = stringResource(R.string.btn_sample_scan),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }

    // Image & Scanning Radar Area
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFF0F0F14))
          .border(1.dp, Color(0xFF282833), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
      ) {
        if (scannerState.imageUri != null) {
          AsyncImage(
            model = scannerState.imageUri,
            contentDescription = "Selected TikTok Live Screenshot",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        // Overlay when scanning or empty
        if (scannerState.isScanning) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color(0xCC000000)),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(
                color = TikTokCyan,
                modifier = Modifier.size(44.dp),
                strokeWidth = 3.dp
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = stringResource(R.string.scanning_status),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = TikTokCyan
              )
            }

            // Laser bar animation
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.TopCenter)
                .padding(top = (180 * scanLaserOffset).dp)
                .background(
                  Brush.horizontalGradient(
                    listOf(TikTokPink, TikTokCyan, Color.White, TikTokCyan, TikTokPink)
                  )
                )
            )
          }
        } else if (scannerState.imageUri == null && scannerState.detectedItems.isEmpty()) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Science,
              contentDescription = null,
              tint = Color(0xFF555566),
              modifier = Modifier.size(38.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = stringResource(R.string.no_image_selected),
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF7E808F),
              textAlign = TextAlign.Center
            )
          }
        } else if (scannerState.imageUri == null && scannerState.detectedItems.isNotEmpty()) {
          // Simulation banner
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.linearGradient(
                  colors = listOf(Color(0xFF1E1020), Color(0xFF0F1E28))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "⚡ Live Stream Frame Simulation",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TikTokCyan
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Gifts & Coin drop detected live on screen",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFAAAAAF)
              )
            }
          }
        }
      }
    }

    // Feedback message
    if (scannerState.infoMessage != null) {
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SuccessGreen.copy(alpha = 0.15f))
            .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = scannerState.infoMessage,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = SuccessGreen
          )
        }
      }
    }

    // Summary Financial Totals Card
    if (scannerState.detectedItems.isNotEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF191922)),
          border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CoinGold, TikTokPink)))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = stringResource(R.string.scan_result_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )

              FilledTonalButton(
                onClick = onSaveSession,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = TikTokPink.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_save_session")
              ) {
                Icon(
                  imageVector = Icons.Default.Bookmark,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = stringResource(R.string.btn_save_session),
                  fontSize = 11.sp,
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main 2x2 stats grid
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Total Coins
              StatBox(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.total_coins_detected),
                value = String.format(Locale.US, "%,d", scannerState.totalCoins),
                subtitle = "Total Stream Coins",
                accentColor = CoinGold,
                icon = Icons.Default.MonetizationOn
              )

              // Diamonds
              StatBox(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.creator_diamonds),
                value = String.format(Locale.US, "%,d", scannerState.totalDiamonds),
                subtitle = "Creator Diamonds",
                accentColor = DiamondCyan,
                icon = Icons.Default.Diamond
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Creator Net Cash Payout
              StatBox(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.est_creator_payout),
                value = currency.formatCreatorPayout(scannerState.totalDiamonds),
                subtitle = "Net Cash Payout (50%)",
                accentColor = SuccessGreen,
                icon = Icons.Default.Savings
              )

              // Estimated Buyer Cost
              StatBox(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.est_buyer_cost),
                value = currency.formatBuyerCost(scannerState.totalCoins),
                subtitle = "Retail Fan Cost",
                accentColor = TikTokPink,
                icon = Icons.Default.Payments
              )
            }
          }
        }
      }

      // Quick Add Gift Carousel
      item {
        Column {
          Text(
            text = stringResource(R.string.quick_add_label),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFC0C1CD),
            modifier = Modifier.padding(bottom = 8.dp)
          )
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val quickGifts = TikTokGiftData.ALL_GIFTS.take(8)
            items(quickGifts) { gift ->
              QuickAddChip(
                gift = gift,
                onClick = { onAddGift(gift) }
              )
            }
          }
        }
      }

      // Detected Gifts Header
      item {
        Text(
          text = stringResource(R.string.detected_gifts_label) + " (${scannerState.detectedItems.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color.White
        )
      }

      // List of detected gifts
      items(scannerState.detectedItems, key = { it.gift.id }) { item ->
        DetectedGiftRow(
          item = item,
          currency = currency,
          onIncrement = { onAdjustQuantity(item.gift.id, 1) },
          onDecrement = { onAdjustQuantity(item.gift.id, -1) }
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun StatBox(
  modifier: Modifier = Modifier,
  title: String,
  value: String,
  subtitle: String,
  accentColor: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF22222D))
      .border(1.dp, Color(0xFF333342), RoundedCornerShape(12.dp))
      .padding(12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          fontSize = 11.sp,
          color = Color(0xFFA5A6B4),
          fontWeight = FontWeight.Medium
        )
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = accentColor,
          modifier = Modifier.size(16.dp)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
        color = accentColor
      )

      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = Color(0xFF787985)
      )
    }
  }
}

@Composable
private fun QuickAddChip(
  gift: TikTokGift,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0xFF22222C))
      .border(1.dp, Color(0xFF333344), RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 6.dp)
      .testTag("quick_add_${gift.id}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = gift.emoji, fontSize = 14.sp)
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = gift.name,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = Color.White
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = "+",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = TikTokPink
    )
  }
}

@Composable
private fun DetectedGiftRow(
  item: DetectedGiftItem,
  currency: AppCurrency,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B24)),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFF2F303F), Color(0xFF1B1B24))))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Gift Emoji / Icon
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF262635)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = item.gift.emoji, fontSize = 24.sp)
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Gift Details
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = item.gift.name,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "(${item.gift.greekName})",
            fontSize = 11.sp,
            color = Color(0xFF8E8F9B)
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = String.format(Locale.US, "%,d coins ea", item.gift.coinCost),
            fontSize = 11.sp,
            color = CoinGold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "•",
            fontSize = 10.sp,
            color = Color(0xFF555566)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = currency.formatCreatorPayout(item.subtotalDiamonds),
            fontSize = 11.sp,
            color = SuccessGreen,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      // Quantity Controls
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF272736))
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        IconButton(
          onClick = onDecrement,
          modifier = Modifier
            .size(28.dp)
            .testTag("btn_dec_${item.gift.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = "Decrease count",
            tint = Color(0xFFB0B0C0),
            modifier = Modifier.size(16.dp)
          )
        }

        Text(
          text = "${item.quantity}",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          modifier = Modifier.padding(horizontal = 8.dp)
        )

        IconButton(
          onClick = onIncrement,
          modifier = Modifier
            .size(28.dp)
            .testTag("btn_inc_${item.gift.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Increase count",
            tint = TikTokPink,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}
