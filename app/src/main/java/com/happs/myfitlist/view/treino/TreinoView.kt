package com.happs.myfitlist.view.treino

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.happs.myfitlist.R
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.MyYellow
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.tela_treino.CustomCardDiaTreino
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.TreinoViewModel

@Composable
fun TreinoView(
    navController: NavHostController,
    viewModel: TreinoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.treinoState.collectAsState()

    var expandedPlanoTreinoList by remember { mutableStateOf(false) }

    val listPlanoTreinoState = rememberLazyListState()
    val listDiaTreinoState = rememberLazyListState()

    val listPlanoTreino = uiState.listaPlanosTreino
    val listDiaTreino = uiState.diasComExercicios
    val usuario = uiState.usuario
    val planoTreinoPrincipal = uiState.planoTreinoPrincipal

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
                    text = "Olá ${usuario.nome.split(" ")[0]} 💪",
                    fontFamily = myFontTitle,
                    fontSize = 50.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = MyWhite,
                    modifier = Modifier.weight(0.9f)
                )

                OutlinedButton(
                    onClick = { expandedPlanoTreinoList = !expandedPlanoTreinoList },
                    colors = ButtonDefaults.buttonColors(containerColor = MyWhite),
                    shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                    modifier = Modifier
                        .weight(0.2f)
                        .height(50.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = if (!expandedPlanoTreinoList) Icons.Default.Add else Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                            text = "Planos de treino",
                            fontFamily = myFontTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyBlack,
                        )
                        if (listPlanoTreino.isNotEmpty()) {
                            LazyColumn(state = listPlanoTreinoState) {
                                items(listPlanoTreino) {
                                    Card(
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .background(MyBlack.copy(0.1f))
                                                .padding(5.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(modifier = Modifier
                                                .clickable {
                                                    viewModel.atualizarPlanoTreinoPrincipal(
                                                        usuario.id,
                                                        it.id
                                                    )
                                                }) {
                                                Icon(
                                                    tint = MyYellow,
                                                    painter = painterResource(id = if (planoTreinoPrincipal.id == it.id) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24),
                                                    contentDescription = "Selecionar como principal",
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .padding(end = 5.dp)
                                                )
                                                Text(
                                                    text = it.nome,
                                                    fontFamily = myFontBody,
                                                    fontSize = 15.sp,
                                                    color = MyBlack,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Icon(
                                                modifier = Modifier.clickable {
                                                    viewModel.excluirPlanoTreino(it)
                                                },
                                                tint = MyBlack,
                                                painter = painterResource(id = R.drawable.baseline_close_24),
                                                contentDescription = "Remover exercício",
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        OutlinedButton(
                            onClick = {
                                navController.navigate("criar_plano_treino") {
                                    launchSingleTop = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
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

        if (!expandedPlanoTreinoList && usuario.idPlanoTreinoPrincipal != -1) {
            Card(onClick = {
                navController.navigate("editar_plano/${usuario.idPlanoTreinoPrincipal}") {
                    launchSingleTop = true
                }
            }, shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(MyRed.copy(0.6f))
                        .padding(start = 10.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = planoTreinoPrincipal.nome,
                        fontFamily = myFontTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 22.sp,
                        color = MyWhite,
                    )
                    Icon(
                        tint = MyWhite,
                        painter = painterResource(id = R.drawable.baseline_edit_24),
                        contentDescription = "Editar plano",
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        if (listDiaTreino.isNotEmpty()) {
            LazyColumn(state = listDiaTreinoState) {
                items(listDiaTreino) {
                    CustomCardDiaTreino(
                        diaTreino = it.first,
                        exercicioList = it.second,
                        onClick = {})
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nenhum plano de treino selecionado",
                    textAlign = TextAlign.Center,
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyWhite,
                )
            }
        }
    }
}