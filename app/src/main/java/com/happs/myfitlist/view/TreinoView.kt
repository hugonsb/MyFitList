package com.happs.myfitlist.view

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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.CustomCard

@Composable
fun TreinoView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
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
                onClick = {},
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

        Text(
            text = "Plano de treino 1",
            fontFamily = myFontBody,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MyWhite,
        )

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

        if (listDiaTreino.isNotEmpty()) {
            LazyColumn {
                items(listDiaTreino) {
                    CustomCard(
                        diaTreino = it,
                        exercicioList = listExercicio,
                        onClick = {})
                }
            }
        }
    }
}