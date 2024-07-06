package com.happs.myfitlist.view.treino

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.happs.myfitlist.R
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.navigation.canGoBack
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.TextFieldColors
import com.happs.myfitlist.ui.theme.TextFieldColors.colorsTextFieldsCard
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.CustomAlertDialog
import com.happs.myfitlist.util.CustomTopAppBar
import com.happs.myfitlist.util.DiasList
import com.happs.myfitlist.util.pager.PageIndicator
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.treino.EditarPlanoTreinoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditarPlanoTreinoView(
    navController: NavHostController,
    planoTreinoId: Int,
    viewModel: EditarPlanoTreinoViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.editarPlanoState.collectAsState()

    var isNomePlanoTreinoEror by rememberSaveable { mutableStateOf(false) }

    var enabledButton by remember { mutableStateOf(true) }

    val ctx = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { DiasList.dias.size })
    val scrollState = rememberScrollState()

    val openDialog = remember { mutableStateOf(false) }

    BackHandler {
        openDialog.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (openDialog.value) {
            CustomAlertDialog(
                title = stringResource(R.string.deseja_voltar),
                text = stringResource(R.string.as_alteracoes_serao_perdidas),
                textButtomConfirm = stringResource(R.string.voltar),
                onclose = { openDialog.value = false },
                onConfirm = {
                    if (navController.canGoBack) {
                        navController.popBackStack("treino", false)
                    }
                    openDialog.value = false
                }
            )
        }

        CustomTopAppBar(onBackPressed = {
            openDialog.value = true
        }, barTitle = stringResource(R.string.editar_plano_de_treino))

        OutlinedTextField(
            value = uiState.nomePlanoTreino,
            onValueChange = {
                if (it.length <= 100) {
                    viewModel.setNomePlanoTreino(it)
                    isNomePlanoTreinoEror = false
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = isNomePlanoTreinoEror,
            supportingText = { if (isNomePlanoTreinoEror) Text("Digite um nome para o plano") },
            placeholder = {
                Text(
                    text = "Nome do plano",
                    fontFamily = myFontBody,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldColors.colorsTextFields(),
            textStyle = TextStyle(
                fontFamily = myFontBody,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
        )

        PageIndicator(
            pageCount = DiasList.dias.size,
            currentPage = pagerState.currentPage,
            pagerState = pagerState,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    key = { pageIndex -> pageIndex }
                ) { currentPage ->
                    CustomCardEditarDiaSemanaa(currentPage)
                }
            }
        }

        if (enabledButton) {
            FilledTonalButton(
                onClick = {
                    coroutineScope.launch {
                        enabledButton = false
                        val (success, message) = viewModel.editarPlanoTreino(planoTreinoId)
                        if (success) {
                            navController.popBackStack("treino", false)
                        } else {
                            enabledButton = true
                        }
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MyWhite),
                shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(70.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "SALVAR ALTERAÇÕES",
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun CustomCardEditarDiaSemanaa(
    indiceDia: Int,
    viewModel: EditarPlanoTreinoViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {

    // tirar o foco do campo de texto ao trocar de card
    LocalFocusManager.current.clearFocus()

    val uiState by viewModel.editarPlanoState.collectAsState()

    var isGrupoMuscularError by rememberSaveable { mutableStateOf(false) }

    var enabledButton by remember { mutableStateOf(true) }

    val openDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyWhite)
                .padding(10.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 5.dp)
                    .align(Alignment.Start),
                text = DiasList.dias[indiceDia],
                fontFamily = myFontTitle,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MyBlack,
            )

            Column {
                OutlinedTextField(
                    value = uiState.grupoMuscular[indiceDia],
                    onValueChange = {
                        if (it.length <= 100) {
                            viewModel.setGrupoMuscular(indiceDia, it)
                            isGrupoMuscularError = false
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = isGrupoMuscularError,
                    supportingText = { if (isGrupoMuscularError) Text(stringResource(R.string.informe_o_grupo_muscular)) },
                    placeholder = {
                        Text(
                            text = "Grupo muscular",
                            fontFamily = myFontBody,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = colorsTextFieldsCard(),
                    textStyle = TextStyle(
                        fontFamily = myFontBody,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                )

                uiState.exerciciosList[indiceDia].forEach { exercicio ->
                    Card(
                        modifier = Modifier.padding(bottom = 10.dp),
                        shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f),
                                text = "${exercicio.nome} ${exercicio.numeroSeries}x${exercicio.numeroRepeticoes}",
                                fontFamily = myFontBody,
                                fontSize = 15.sp,
                                color = MyWhite,
                            )
                            Icon(
                                modifier = Modifier
                                    .clickable {
                                        viewModel.removerExercicio(indiceDia, exercicio)
                                    },
                                tint = MyWhite,
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Remover exercício",
                            )
                        }
                    }
                }

                if (openDialog.value) {
                    CustomAlertDialogEditarExercicio(
                        title = stringResource(R.string.adicionar_exercicio),
                        dia = indiceDia,
                        onClickOk = {
                            openDialog.value = false
                            enabledButton = true
                        },
                        onClickCancelar = {
                            openDialog.value = false
                            enabledButton = true
                        })
                }

                OutlinedButton(
                    onClick = {
                        enabledButton = false
                        openDialog.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyRed,
                        disabledContainerColor = MyRed.copy(0.5f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = stringResource(R.string.adicionar_exerc_cio),
                        fontFamily = myFontTitle,
                        fontSize = 18.sp,
                        color = MyWhite,
                    )
                }

            }
        }
    }
}

@Composable
fun CustomAlertDialogEditarExercicio(
    title: String,
    dia: Int,
    onClickOk: () -> Unit,
    onClickCancelar: () -> Unit,
    viewiewModel: EditarPlanoTreinoViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState by viewiewModel.editarPlanoState.collectAsState()

    var isNomeExercicioError by rememberSaveable { mutableStateOf(false) }
    var isNumeroSeriesError by rememberSaveable { mutableStateOf(false) }
    var isNumeroRepeticoesError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            onClickCancelar()
        },
        title = {
            Text(
                title,
                fontSize = 35.sp,
                color = MyBlack,
                textAlign = TextAlign.Center,
                fontFamily = myFontTitle,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = uiState.nomeExercicio[dia],
                    onValueChange = {
                        if (it.length <= 100) {
                            viewiewModel.setNomeExercicio(dia, it)
                            isNomeExercicioError = false
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = isNomeExercicioError,
                    supportingText = { if (isNomeExercicioError) Text(stringResource(R.string.campo_obrigat_rio)) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.nome_do_exercicio),
                            fontFamily = myFontBody,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = colorsTextFieldsCard(),
                    textStyle = TextStyle(
                        fontFamily = myFontBody,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                )

                Row {
                    val pattern = remember { Regex("^\\d*\$") }
                    OutlinedTextField(
                        value = uiState.numeroSeries[dia],
                        onValueChange = {
                            if (it.matches(pattern)) {
                                if (it.isNotEmpty()) {
                                    viewiewModel.setNumeroSeries(
                                        dia, it.toInt().coerceAtMost(15).toString()
                                    )
                                } else {
                                    viewiewModel.setNumeroSeries(dia, it)
                                }
                                isNumeroSeriesError = false
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = isNumeroSeriesError,
                        supportingText = { if (isNumeroSeriesError) Text(stringResource(id = R.string.campo_obrigat_rio)) },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.series),
                                fontFamily = myFontBody,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = colorsTextFieldsCard(),
                        textStyle = TextStyle(
                            fontFamily = myFontBody,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = uiState.numeroRepeticoes[dia],
                        onValueChange = {
                            if (it.matches(pattern)) {
                                if (it.isNotEmpty()) {
                                    viewiewModel.setNumeroRepeticoes(
                                        dia,
                                        it.toInt().coerceAtMost(40).toString()
                                    )
                                } else {
                                    viewiewModel.setNumeroRepeticoes(dia, it)
                                }
                                isNumeroRepeticoesError = false
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = isNumeroRepeticoesError,
                        supportingText = { if (isNumeroRepeticoesError) Text(stringResource(id = R.string.campo_obrigat_rio)) },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.repeticoes),
                                fontFamily = myFontBody,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = colorsTextFieldsCard(),
                        textStyle = TextStyle(
                            fontFamily = myFontBody,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedButton(
                    onClick = {
                        val nomeExercicio = uiState.nomeExercicio[dia]
                        val numeroSeries = uiState.numeroSeries[dia]
                        val numeroRepeticoes = uiState.numeroRepeticoes[dia]

                        if (nomeExercicio.isNotEmpty() && numeroSeries.isNotEmpty() && numeroRepeticoes.isNotEmpty()) {

                            viewiewModel.adicionarExercicio(
                                dia,
                                Exercicio(
                                    nome = nomeExercicio,
                                    numeroSeries = numeroSeries.toInt(),
                                    numeroRepeticoes = numeroRepeticoes.toInt(),
                                    idDiaTreino = -1
                                )
                            )

                            viewiewModel.setNomeExercicio(dia, "")
                            viewiewModel.setNumeroSeries(dia, "")
                            viewiewModel.setNumeroRepeticoes(dia, "")
                            onClickOk()
                        } else {
                            if (nomeExercicio.isEmpty()) {
                                isNomeExercicioError = true
                            }
                            if (numeroSeries.isEmpty()) {
                                isNumeroSeriesError = true
                            }
                            if (numeroRepeticoes.isEmpty()) {
                                isNumeroRepeticoesError = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(120.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        fontFamily = myFontTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyWhite,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = stringResource(id = R.string.cancelar), fontFamily = myFontTitle,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyBlack,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onClickCancelar() }
                )
            }
        },
        confirmButton = {},
        dismissButton = {},
    )
}