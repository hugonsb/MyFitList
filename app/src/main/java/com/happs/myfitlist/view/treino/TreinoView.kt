package com.happs.myfitlist.view.treino

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.CustomCardDiaTreino
import com.happs.myfitlist.util.CustomCardPlanoTreino

@Composable
fun TreinoView(navController: NavHostController) {

    var expandedPlanoTreinoList by remember { mutableStateOf(false) }

    // Variável contendo planos de treino de exemplo
    val listPlanoTreino = listOf(
        PlanoTreino(id = 1, nome = "Treino do meu amigo trembolonado", idUsuario = 1),
        PlanoTreino(id = 2, nome = "Treino do Renato Cariri lesionado", idUsuario = 1),
        PlanoTreino(id = 3, nome = "Treino do LALA", idUsuario = 1),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp, top = 5.dp),
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Olá Fulano 💪",
                    fontFamily = myFontTitle,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyWhite,
                )

                OutlinedButton(
                    onClick = { expandedPlanoTreinoList = !expandedPlanoTreinoList },
                    colors = ButtonDefaults.buttonColors(containerColor = MyWhite),
                    shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(70.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (expandedPlanoTreinoList) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = CutCornerShape(topStart = 15.dp, bottomEnd = 15.dp),
                    colors = CardColors(
                        containerColor = MyWhite,
                        contentColor = MyWhite,
                        disabledContainerColor = MyWhite,
                        disabledContentColor = MyWhite
                    )
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                            text = "Planos de treino",
                            fontFamily = myFontTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyBlack,
                        )
                        val listPlanoTreinoState = rememberLazyListState()
                        if (listPlanoTreino.isNotEmpty()) {
                            LazyColumn(state = listPlanoTreinoState) {
                                items(listPlanoTreino) {
                                    CustomCardPlanoTreino(
                                        it,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                        OutlinedButton(
                            onClick = { navController.navigate("criar_plano_treino") { launchSingleTop = true } },
                            colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            //contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "NOVO PLANO",
                                fontFamily = myFontTitle,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyWhite,
                            )
                        }
                    }
                }
            }
        }

        if (!expandedPlanoTreinoList) {
            Text(
                text = "Plano de treino 1",
                fontFamily = myFontTitle,
                fontSize = 22.sp,
                color = MyWhite,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Variável contendo os dias da semana de exemplo
        val listDiaTreino = listOf(
            DiaTreino(id = 1, dia = "Segunda-feira", grupoMuscular = "Peito", idPlanoTreino = 1),
            DiaTreino(id = 2, dia = "Terça-feira", grupoMuscular = "Costas", idPlanoTreino = 1),
            DiaTreino(id = 3, dia = "Quarta-feira", grupoMuscular = "Pernas", idPlanoTreino = 1),
            DiaTreino(id = 4, dia = "Quinta-feira", grupoMuscular = "Ombros", idPlanoTreino = 1),
            DiaTreino(id = 5, dia = "Sexta-feira", grupoMuscular = "Braços", idPlanoTreino = 1),
            DiaTreino(id = 6, dia = "Sábado", grupoMuscular = "Abdômen", idPlanoTreino = 1),
            DiaTreino(id = 7, dia = "Domingo", grupoMuscular = "Descanso", idPlanoTreino = 1)
        )

        // Variável contendo uma lista de exercícios de exemplo
        val listExercicio = listOf(
            Exercicio(
                nome = "Supino Reto",
                numeroSeries = 3,
                numeroRepeticoes = 10,
                idDiaTreino = 1
            ),
            Exercicio(
                nome = "Crucifixo Inclinado",
                numeroSeries = 3,
                numeroRepeticoes = 12,
                idDiaTreino = 1
            ),
            Exercicio(
                nome = "Puxada Alta",
                numeroSeries = 3,
                numeroRepeticoes = 10,
                idDiaTreino = 2
            ),
            Exercicio(
                nome = "Remada Curvada",
                numeroSeries = 3,
                numeroRepeticoes = 12,
                idDiaTreino = 2
            ),
            Exercicio(
                nome = "Agachamento",
                numeroSeries = 3,
                numeroRepeticoes = 12,
                idDiaTreino = 3
            ),
            Exercicio(nome = "Leg Press", numeroSeries = 3, numeroRepeticoes = 15, idDiaTreino = 3),
            Exercicio(
                nome = "Desenvolvimento com Halteres",
                numeroSeries = 3,
                numeroRepeticoes = 10,
                idDiaTreino = 4
            ),
            Exercicio(
                nome = "Elevação Lateral",
                numeroSeries = 3,
                numeroRepeticoes = 12,
                idDiaTreino = 4
            ),
            Exercicio(
                nome = "Rosca Direta",
                numeroSeries = 3,
                numeroRepeticoes = 10,
                idDiaTreino = 5
            ),
            Exercicio(
                nome = "Tríceps Testa",
                numeroSeries = 3,
                numeroRepeticoes = 12,
                idDiaTreino = 5
            ),
            Exercicio(
                nome = "Abdominal Supra",
                numeroSeries = 3,
                numeroRepeticoes = 15,
                idDiaTreino = 6
            ),
            Exercicio(nome = "Prancha", numeroSeries = 3, numeroRepeticoes = 60, idDiaTreino = 6)
        )

        val listDiaTreinoState = rememberLazyListState()
        if (listDiaTreino.isNotEmpty()) {
            LazyColumn(state = listDiaTreinoState) {
                items(listDiaTreino) {
                    CustomCardDiaTreino(
                        diaTreino = it,
                        exercicioList = listExercicio,
                        onClick = {})
                }
            }
        }
    }
}