package com.happs.myfitlist.util

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.R
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle

@Composable
fun CustomCard(diaTreino: DiaTreino, exercicioList: List<Exercicio>, onClick: () -> Unit) {

    //testa o saveable dps
    var expanded by rememberSaveable { mutableStateOf(false) }
    var titleOverflow by remember { mutableStateOf(false) }
    var detailsOverflow by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .padding(bottom = 10.dp),
        shape = CutCornerShape(topStart = 15.dp, bottomEnd = 15.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .background(MyWhite)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = diaTreino.dia,
                    fontFamily = myFontTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyBlack,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult: TextLayoutResult ->
                        titleOverflow = textLayoutResult.hasVisualOverflow
                    }
                )
                Text(
                    text = diaTreino.grupoMuscular,
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyRed,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult: TextLayoutResult ->
                        titleOverflow = textLayoutResult.hasVisualOverflow
                    }
                )
                val exerciseText = exercicioList.joinToString(separator = "\n") { exercicio ->
                    "${exercicio.nome} ${exercicio.numeroSeries}x${exercicio.numeroRepeticoes}"
                }
                Text(
                    text = exerciseText,
                    fontFamily = myFontBody,
                    fontSize = 15.sp,
                    color = MyBlack,
                    maxLines = if (expanded) Int.MAX_VALUE else 6,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult: TextLayoutResult ->
                        detailsOverflow = textLayoutResult.hasVisualOverflow
                    }
                )
            }

            if ((titleOverflow || detailsOverflow) && !expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = MyBlack,
                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }
            } else if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ver menos",
                        fontFamily = myFontTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyBlack
                    )
                }
            }
        }
    }
}