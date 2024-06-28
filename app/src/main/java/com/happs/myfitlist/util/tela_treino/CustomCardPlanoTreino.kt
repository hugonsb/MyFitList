package com.happs.myfitlist.util.tela_treino

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.R
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyYellow
import com.happs.myfitlist.ui.theme.myFontBody

@Composable
fun CustomCardPlanoTreino(planoTreino: PlanoTreino, onClick: () -> Unit) {

    var starClick by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.clickable { starClick = !starClick },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            tint = MyYellow,
            painter = painterResource(id = if (!starClick) R.drawable.baseline_star_border_24 else R.drawable.baseline_star_24),
            contentDescription = "Selecionar como principal",
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = planoTreino.nome,
            fontFamily = myFontBody,
            fontSize = 15.sp,
            //fontWeight = FontWeight.Bold,
            color = MyBlack,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}