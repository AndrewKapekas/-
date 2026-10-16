package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.TikTokPink
import com.example.viewmodel.AppTab

@Composable
fun TokBottomNav(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    containerColor = Color(0xFF101014),
    tonalElevation = 8.dp
  ) {
    NavigationBarItem(
      selected = currentTab == AppTab.SCANNER,
      onClick = { onTabSelected(AppTab.SCANNER) },
      icon = {
        Icon(
          imageVector = Icons.Default.QrCodeScanner,
          contentDescription = stringResource(R.string.nav_scanner)
        )
      },
      label = {
        Text(
          text = stringResource(R.string.nav_scanner),
          fontSize = 11.sp,
          fontWeight = if (currentTab == AppTab.SCANNER) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = TikTokPink,
        indicatorColor = TikTokPink.copy(alpha = 0.85f),
        unselectedIconColor = Color(0xFF888894),
        unselectedTextColor = Color(0xFF888894)
      ),
      modifier = Modifier.testTag("nav_tab_scanner")
    )

    NavigationBarItem(
      selected = currentTab == AppTab.GIFTS,
      onClick = { onTabSelected(AppTab.GIFTS) },
      icon = {
        Icon(
          imageVector = Icons.Default.CardGiftcard,
          contentDescription = stringResource(R.string.nav_gifts)
        )
      },
      label = {
        Text(
          text = stringResource(R.string.nav_gifts),
          fontSize = 11.sp,
          fontWeight = if (currentTab == AppTab.GIFTS) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = TikTokPink,
        indicatorColor = TikTokPink.copy(alpha = 0.85f),
        unselectedIconColor = Color(0xFF888894),
        unselectedTextColor = Color(0xFF888894)
      ),
      modifier = Modifier.testTag("nav_tab_gifts")
    )

    NavigationBarItem(
      selected = currentTab == AppTab.CALCULATOR,
      onClick = { onTabSelected(AppTab.CALCULATOR) },
      icon = {
        Icon(
          imageVector = Icons.Default.Calculate,
          contentDescription = stringResource(R.string.nav_calculator)
        )
      },
      label = {
        Text(
          text = stringResource(R.string.nav_calculator),
          fontSize = 11.sp,
          fontWeight = if (currentTab == AppTab.CALCULATOR) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = TikTokPink,
        indicatorColor = TikTokPink.copy(alpha = 0.85f),
        unselectedIconColor = Color(0xFF888894),
        unselectedTextColor = Color(0xFF888894)
      ),
      modifier = Modifier.testTag("nav_tab_calculator")
    )

    NavigationBarItem(
      selected = currentTab == AppTab.CHEST,
      onClick = { onTabSelected(AppTab.CHEST) },
      icon = {
        Icon(
          imageVector = Icons.Default.Timer,
          contentDescription = stringResource(R.string.nav_chest)
        )
      },
      label = {
        Text(
          text = stringResource(R.string.nav_chest),
          fontSize = 11.sp,
          fontWeight = if (currentTab == AppTab.CHEST) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = TikTokPink,
        indicatorColor = TikTokPink.copy(alpha = 0.85f),
        unselectedIconColor = Color(0xFF888894),
        unselectedTextColor = Color(0xFF888894)
      ),
      modifier = Modifier.testTag("nav_tab_chest")
    )

    NavigationBarItem(
      selected = currentTab == AppTab.HISTORY,
      onClick = { onTabSelected(AppTab.HISTORY) },
      icon = {
        Icon(
          imageVector = Icons.Default.History,
          contentDescription = stringResource(R.string.nav_history)
        )
      },
      label = {
        Text(
          text = stringResource(R.string.nav_history),
          fontSize = 11.sp,
          fontWeight = if (currentTab == AppTab.HISTORY) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = TikTokPink,
        indicatorColor = TikTokPink.copy(alpha = 0.85f),
        unselectedIconColor = Color(0xFF888894),
        unselectedTextColor = Color(0xFF888894)
      ),
      modifier = Modifier.testTag("nav_tab_history")
    )
  }
}
