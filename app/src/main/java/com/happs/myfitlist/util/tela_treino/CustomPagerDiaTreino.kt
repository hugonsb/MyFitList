package com.happs.myfitlist.util.tela_treino

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.happs.myfitlist.util.DiasList
import com.happs.myfitlist.util.pager.PageIndicator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomPagerDiaTreino(listDiaTreino: Array<Pair<DiaTreino, List<Exercicio>>>) {

    val pagerState = rememberPagerState(pageCount = { DiasList.dias.size })
    val scrollState = rememberScrollState()

    Column {
        PageIndicator(
            pageCount = DiasList.dias.size,
            currentPage = pagerState.currentPage,
            pagerState = pagerState,
            modifier = Modifier
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 75.dp),
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    key = { pageIndex -> pageIndex }
                ) { currentPage ->
                    Card(
                        shape = CutCornerShape(topStart = 15.dp, bottomEnd = 15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MyWhite)
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = listDiaTreino[currentPage].first.dia,
                                fontFamily = myFontTitle,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyBlack,
                            )
                            Text(
                                text = listDiaTreino[currentPage].first.grupoMuscular,
                                fontFamily = myFontTitle,
                                fontSize = 29.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyRed,
                            )

                            for ((index, exercicio) in listDiaTreino[currentPage].second.withIndex()) {
                                Row(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        tint = MyRed,
                                        painter = painterResource(id = R.drawable.dumbbell_icon),
                                        contentDescription = "Imagem do exercício",
                                        modifier = Modifier.size(40.dp)
                                    )

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Column {
                                        Text(
                                            text = exercicio.nome,
                                            fontFamily = myFontBody,
                                            fontSize = 20.sp,
                                            lineHeight = 19.sp,
                                            color = MyBlack,
                                        )
                                        Text(
                                            text = "SÉRIES: ${exercicio.numeroSeries} REPS.: ${exercicio.numeroRepeticoes}",
                                            fontFamily = myFontBody,
                                            fontSize = 12.sp,
                                            lineHeight = 19.sp,
                                            color = MyBlack,
                                        )
                                    }
                                }

                                if (index < listDiaTreino[currentPage].second.size - 1) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(0.6.dp)
                                            .background(Color.Gray)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}