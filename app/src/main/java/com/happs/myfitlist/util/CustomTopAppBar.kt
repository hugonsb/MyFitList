package com.happs.myfitlist.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.ui.theme.myFontTitle

@Composable
fun CustomTopAppBar(onBackPressed: () -> Unit, barTitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .size(45.dp),
            contentPadding = PaddingValues(0.dp),
            onClick = {
                onBackPressed()
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
            elevation = ButtonDefaults.buttonElevation(10.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Voltar",
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
            text = barTitle,
            fontFamily = myFontTitle,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}