package com.happs.myfitlist.view.treino

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.happs.myfitlist.R
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario
import com.happs.myfitlist.room.RepositoryResponse
import com.happs.myfitlist.state.TreinoState
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.MyYellow
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.CustomAlertDialog
import com.happs.myfitlist.util.DiasList
import com.happs.myfitlist.util.ErrorScreen
import com.happs.myfitlist.util.LoadingScreen
import com.happs.myfitlist.util.func.getCurrentDayOfWeekIndex
import com.happs.myfitlist.util.pager.PageIndicator
import com.happs.myfitlist.viewmodel.treino.TreinoViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TreinoView(
    navController: NavHostController,
    treinoViewModel: TreinoViewModel = koinViewModel<TreinoViewModel>()
) {

    val ctx = LocalContext.current

    //funcionalidade "toque novamente pra sair"
    var backPressedOnce = false
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Fecha o aplicativo ao clicar em voltar em menos de 2s
                if (backPressedOnce) {
                    (ctx as? Activity)?.finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(
                        ctx,
                        R.string.toque_em_voltar_sair,
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed(
                        { backPressedOnce = false },
                        2000
                    )
                }
            }
        }
    }

    // callback para interceptar o evento de voltar
    DisposableEffect(onBackPressedDispatcher) {
        onBackPressedDispatcher?.onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }

    val uiState by treinoViewModel.treinoState.collectAsState()

    when (val state = uiState) {
        is RepositoryResponse.Loading -> {
            LoadingScreen()
        }

        is RepositoryResponse.Success -> {
            TreinoViewContent(state, treinoViewModel, navController)
        }

        is RepositoryResponse.Error -> {
            ErrorScreen()
        }
    }
}

@Composable
fun TreinoViewContent(
    state: RepositoryResponse.Success<TreinoState>,
    treinoViewModel: TreinoViewModel,
    navController: NavHostController
) {
    var expandedPlanoTreinoList by remember { mutableStateOf(false) }
    val listPlanoTreinoState = rememberLazyListState()
    val usuario = state.data.usuario
    val uiState = state.data
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp, top = 5.dp),
    ) {
        Header(usuario = usuario)
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            if (expandedPlanoTreinoList && uiState.listaPlanosTreino.isNotEmpty()) {
                PlanosTreinoList(
                    listPlanoTreino = uiState.listaPlanosTreino,
                    planoTreinoPrincipal = uiState.planoTreinoPrincipal,
                    listPlanoTreinoState = listPlanoTreinoState,
                    usuario = usuario,
                    treinoViewModel = treinoViewModel,
                    selecionarPlano = { expandedPlanoTreinoList = false }
                )
            } else if (usuario.idPlanoTreinoPrincipal != -1) {
                PlanoTreinoPrincipal(
                    planoTreinoPrincipal = uiState.planoTreinoPrincipal,
                    usuario = usuario,
                    navController = navController,
                    clickPlano = { expandedPlanoTreinoList = true }
                )
            }
        }

        Box {

            DiasTreinoList(listDiaTreino = uiState.diasComExercicios)

            FloatingActionButton(modifier = Modifier
                .padding(bottom = 10.dp)
                .size(55.dp)
                .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.onSecondary,
                contentColor = MyWhite,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 3.dp),
                shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                onClick = {
                    navController.navigate("criar_plano_treino") { launchSingleTop = true }
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun Header(usuario: Usuario) {
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
    }
}

@Composable
fun PlanoTreinoPrincipal(
    planoTreinoPrincipal: PlanoTreino,
    usuario: Usuario,
    navController: NavHostController,
    clickPlano: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
    ) {
        Card(
            onClick = { clickPlano() },
            modifier = Modifier.weight(1f),
            shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .padding(start = 10.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = planoTreinoPrincipal.nome,
                        fontFamily = myFontTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 28.sp,
                        color = MyWhite
                    )
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                        contentDescription = "Ver todos os planos de treino",
                        tint = MyWhite
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Card(
            onClick = {
                navController.navigate("editar_plano/${usuario.idPlanoTreinoPrincipal}") {
                    launchSingleTop = true
                }
            },
            shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
        ) {
            Row(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .padding(start = 18.dp, end = 18.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    tint = MyWhite,
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "Editar plano",
                    modifier = Modifier
                        .size(35.dp)
                )
            }
        }
    }
}

@Composable
fun PlanosTreinoList(
    listPlanoTreino: List<PlanoTreino>,
    planoTreinoPrincipal: PlanoTreino,
    listPlanoTreinoState: LazyListState,
    usuario: Usuario,
    treinoViewModel: TreinoViewModel,
    selecionarPlano: () -> Unit
) {

    val openDialog = remember { mutableStateOf(false) }
    val planoTreinoParaExcluir = remember { mutableStateOf<PlanoTreino?>(null) }

    if (openDialog.value && planoTreinoParaExcluir.value != null) {
        CustomAlertDialog(
            title = stringResource(R.string.confirmar),
            text = stringResource(R.string.tem_certeza_que_deseja_excluir),
            textButtomConfirm = stringResource(R.string.confirmar),
            onclose = { openDialog.value = false },
            onConfirm = {
                planoTreinoParaExcluir.value?.let {
                    treinoViewModel.excluirPlanoTreino(it)
                }
                openDialog.value = false
            }
        )
    }

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
                                    .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            treinoViewModel.atualizarPlanoTreinoPrincipal(
                                                usuario.id,
                                                it.id
                                            )
                                            selecionarPlano()
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        tint = MyYellow,
                                        painter = painterResource(id = if (planoTreinoPrincipal.id == it.id) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24),
                                        contentDescription = "Selecionar como principal",
                                        modifier = Modifier
                                            .size(35.dp)
                                            .padding(end = 5.dp)
                                    )
                                    Text(
                                        text = it.nome,
                                        fontFamily = myFontBody,
                                        fontSize = 16.sp,
                                        color = MyWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Icon(
                                    modifier = Modifier.clickable {
                                        planoTreinoParaExcluir.value = it
                                        openDialog.value = true
                                    },
                                    tint = MyWhite,
                                    painter = painterResource(id = R.drawable.baseline_close_24),
                                    contentDescription = "Remover plano",
                                )
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    selecionarPlano()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    tint = MyWhite,
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                    contentDescription = "Fechar lista"
                )
            }
        }
    }
}

@Composable
fun DiasTreinoList(
    listDiaTreino: Array<Pair<DiaTreino, List<Exercicio>>>,
) {
    if (listDiaTreino.size == 7) {
        CustomPagerDiaTreino(listDiaTreino = listDiaTreino)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Experimente criar um plano de treino!",
                textAlign = TextAlign.Center,
                fontFamily = myFontTitle,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MyWhite,
            )
            Text(
                text = "Toque no botão + abaixo para começar",
                textAlign = TextAlign.Center,
                fontFamily = myFontTitle,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MyWhite,
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomPagerDiaTreino(listDiaTreino: Array<Pair<DiaTreino, List<Exercicio>>>) {

    val pagerState = rememberPagerState(
        pageCount = { DiasList.dias.size },
        initialPage = getCurrentDayOfWeekIndex()
    )

    val scrollState = rememberScrollState()

    Column {

        Spacer(modifier = Modifier.height(5.dp))

        PageIndicator(
            pageCount = DiasList.dias.size,
            currentPage = pagerState.currentPage,
            pagerState = pagerState,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 75.dp),
                state = pagerState,
                beyondBoundsPageCount = 7,
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

                        Spacer(modifier = Modifier.height(5.dp))

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

                                Spacer(modifier = Modifier.width(8.dp))

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