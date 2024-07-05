package com.happs.myfitlist.util.pager

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.DiasList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, pagerState: PagerState, modifier: Modifier) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
    ) {
        for (i in 0 until pageCount) {
            IndicatorDots(isSelected = i == currentPage, dia = DiasList.dias[i], onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(i)
                }
            })
        }
    }
}

@Composable
fun IndicatorDots(isSelected: Boolean, dia: String, onClick: () -> Unit) {
    val size = animateDpAsState(targetValue = if (isSelected) 45.dp else 40.dp, label = "")
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MyWhite else MaterialTheme.colorScheme.onSecondary,
        animationSpec = spring(), label = ""
    )

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .size(size.value)
            .clip(CircleShape)
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dia.substring(0, minOf(3, dia.length)),
            fontFamily = myFontTitle,
            fontSize = if (isSelected) 25.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MyRed else MyWhite,
        )
    }
}