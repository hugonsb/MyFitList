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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.navigation.NavHostController
import com.happs.myfitlist.R
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.navigation.canGoBack
import com.happs.myfitlist.room.RepositoryResponse
import com.happs.myfitlist.state.PlanoTreinoState
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
import com.happs.myfitlist.util.ErrorScreen
import com.happs.myfitlist.util.LoadingScreen
import com.happs.myfitlist.util.pager.PageIndicator
import com.happs.myfitlist.viewmodel.treino.EditarPlanoTreinoViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditarPlanoTreinoView(
    navController: NavHostController,
    planoTreinoId: Int,
    editarPlanoTreinoViewModel: EditarPlanoTreinoViewModel = koinViewModel<EditarPlanoTreinoViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by editarPlanoTreinoViewModel.editarPlanoTreinoState.collectAsState()

    var isNomePlanoTreinoError by rememberSaveable { mutableStateOf(false) }

    var enabledButton by remember { mutableStateOf(true) }

    val ctx = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { DiasList.dias.size })

    val localFocusManager = LocalFocusManager.current

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { _ ->
            localFocusManager.clearFocus()
        }
    }

    val openDialog = remember { mutableStateOf(false) }

    BackHandler {
        openDialog.value = true
    }

    when (val state = uiState) {
        is RepositoryResponse.Loading -> {
            LoadingScreen()
        }

        is RepositoryResponse.Success -> {
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

            EditarPlanoTreinoContent(
                editarPlanoTreinoViewModel = editarPlanoTreinoViewModel,
                pagerState = pagerState,
                isNomePlanoTreinoError = isNomePlanoTreinoError,
                enabledButton = enabledButton,
                openDialog = openDialog,
                state = state,
                onNomePlanoChange = { nome ->
                    if (nome.length <= 100) {
                        editarPlanoTreinoViewModel.setNomePlanoTreino(nome)
                        isNomePlanoTreinoError = false
                    }
                },
                onSaveClick = {
                    coroutineScope.launch {
                        enabledButton = false
                        val (success, message) = editarPlanoTreinoViewModel.editarPlanoTreino(
                            planoTreinoId
                        )
                        if (success) {
                            navController.popBackStack("treino", false)
                        } else {
                            enabledButton = true
                        }
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }

        is RepositoryResponse.Error -> {
            ErrorScreen()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditarPlanoTreinoContent(
    editarPlanoTreinoViewModel: EditarPlanoTreinoViewModel,
    pagerState: PagerState,
    isNomePlanoTreinoError: Boolean,
    enabledButton: Boolean,
    openDialog: MutableState<Boolean>,
    state: RepositoryResponse.Success<PlanoTreinoState>,
    onNomePlanoChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(onBackPressed = {
            openDialog.value = true
        }, barTitle = stringResource(R.string.editar_plano_de_treino))

        OutlinedTextField(
            value = state.data.nomePlanoTreino,
            onValueChange = onNomePlanoChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = isNomePlanoTreinoError,
            supportingText = { if (isNomePlanoTreinoError) Text("Digite um nome para o plano") },
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
                    beyondBoundsPageCount = 2,
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    key = { pageIndex -> pageIndex },
                    userScrollEnabled = false
                ) { currentPage ->
                    CustomCardEditarDiaSemanaa(currentPage, editarPlanoTreinoViewModel, state)
                }
            }
        }

        FilledTonalButton(
            enabled = enabledButton,
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MyWhite,
                disabledContainerColor = Color.Gray
            ),
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

@Composable
fun CustomCardEditarDiaSemanaa(
    indiceDia: Int,
    editarPlanoTreinoViewModel: EditarPlanoTreinoViewModel,
    state: RepositoryResponse.Success<PlanoTreinoState>
) {

    val ctx = LocalContext.current

    var isGrupoMuscularError by rememberSaveable { mutableStateOf(false) }

    var enabledButton by remember { mutableStateOf(true) }

    val openDialogAdicionarExercicio = remember { mutableStateOf(false) }
    val openDialogCopy = remember { mutableStateOf(false) }

    if (openDialogAdicionarExercicio.value) {
        CustomAlertDialogEditarExercicio(
            title = stringResource(R.string.adicionar_exercicio),
            dia = indiceDia,
            onClickOk = {
                openDialogAdicionarExercicio.value = false
                enabledButton = true
            },
            onClickCancelar = {
                openDialogAdicionarExercicio.value = false
                enabledButton = true
            },
            editarPlanoTreinoViewModel,
            state
        )
    }

    if (openDialogCopy.value) {
        CustomAlertDialogCopyExercicios(
            indiceDia = indiceDia,
            title = "Copiar exercícios",
            onClickOk = { openDialogCopy.value = false },
            onClickCancelar = { openDialogCopy.value = false },
            editarPlanoTreinoViewModel = editarPlanoTreinoViewModel,
            state = state
        )
    }

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
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, bottom = 5.dp),
                    text = DiasList.dias[indiceDia],
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyBlack,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .clickable {
                            if (state.data.exerciciosList[indiceDia].isNotEmpty()) {
                                openDialogCopy.value = true
                            } else {
                                Toast.makeText(
                                    ctx,
                                    "Não há exercícios para copiar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                    tint = MyBlack,
                    painter = painterResource(id = R.drawable.baseline_content_copy_24),
                    contentDescription = "Copiar exercícios",
                )
            }

            Column {
                OutlinedTextField(
                    value = state.data.grupoMuscular[indiceDia],
                    onValueChange = {
                        if (it.length <= 100) {
                            editarPlanoTreinoViewModel.setGrupoMuscular(indiceDia, it)
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

                state.data.exerciciosList[indiceDia].forEach { exercicio ->
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
                                        editarPlanoTreinoViewModel.removerExercicio(
                                            indiceDia,
                                            exercicio
                                        )
                                    },
                                tint = MyWhite,
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Remover exercício",
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        enabledButton = false
                        openDialogAdicionarExercicio.value = true
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
    editarPlanoTreinoViewModel: EditarPlanoTreinoViewModel,
    state: RepositoryResponse.Success<PlanoTreinoState>
) {
    var isNomeExercicioError by rememberSaveable { mutableStateOf(false) }
    var isNumeroSeriesError by rememberSaveable { mutableStateOf(false) }
    var isNumeroRepeticoesError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        shape = CutCornerShape(topStart = 24.dp, bottomEnd = 24.dp),
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
                    value = state.data.nomeExercicio[dia],
                    onValueChange = {
                        if (it.length <= 100) {
                            editarPlanoTreinoViewModel.setNomeExercicio(dia, it)
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
                        value = state.data.numeroSeries[dia],
                        onValueChange = {
                            if (it.matches(pattern)) {
                                if (it.isNotEmpty()) {
                                    editarPlanoTreinoViewModel.setNumeroSeries(
                                        dia, it.toInt().coerceAtMost(15).toString()
                                    )
                                } else {
                                    editarPlanoTreinoViewModel.setNumeroSeries(dia, it)
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
                        value = state.data.numeroRepeticoes[dia],
                        onValueChange = {
                            if (it.matches(pattern)) {
                                if (it.isNotEmpty()) {
                                    editarPlanoTreinoViewModel.setNumeroRepeticoes(
                                        dia,
                                        it.toInt().coerceAtMost(40).toString()
                                    )
                                } else {
                                    editarPlanoTreinoViewModel.setNumeroRepeticoes(dia, it)
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
                        val nomeExercicio = state.data.nomeExercicio[dia]
                        val numeroSeries = state.data.numeroSeries[dia]
                        val numeroRepeticoes = state.data.numeroRepeticoes[dia]

                        if (nomeExercicio.isNotEmpty() && numeroSeries.isNotEmpty() && numeroRepeticoes.isNotEmpty()) {
                            adicionarExercicio(
                                indiceDia = dia,
                                nomeExercicio,
                                numeroSeries.toInt(),
                                numeroRepeticoes.toInt(),
                                onClickOk,
                                editarPlanoTreinoViewModel
                            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlertDialogCopyExercicios(
    indiceDia: Int,
    title: String,
    onClickOk: () -> Unit,
    onClickCancelar: () -> Unit,
    editarPlanoTreinoViewModel: EditarPlanoTreinoViewModel,
    state: RepositoryResponse.Success<PlanoTreinoState>
) {
    var expanded by remember { mutableStateOf(false) }
    val diasListAtualizada = DiasList.dias.filter { it != DiasList.dias[indiceDia] }
    var selectedItem by remember { mutableStateOf(diasListAtualizada.first()) }

    AlertDialog(
        shape = CutCornerShape(topStart = 24.dp, bottomEnd = 24.dp),
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

                Text(
                    text = "Copiar exercícios de ${DiasList.dias[indiceDia]} para:",
                    color = MyBlack,
                    textAlign = TextAlign.Center,
                    fontFamily = myFontBody,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(15.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedItem,
                        textStyle = TextStyle(
                            fontFamily = myFontBody,
                            fontWeight = FontWeight.Bold,
                            color = MyBlack
                        ),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        diasListAtualizada.forEach { dia ->
                            DropdownMenuItem(
                                {
                                    Text(
                                        text = dia,
                                        fontFamily = myFontBody,
                                        fontWeight = FontWeight.Bold,
                                    )
                                },
                                onClick = {
                                    selectedItem = dia
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                OutlinedButton(
                    onClick = {
                        val listExercicios = state.data.exerciciosList[indiceDia]
                        for (exercicio in listExercicios) {
                            adicionarExercicio(
                                DiasList.dias.indexOf(selectedItem),
                                exercicio.nome,
                                exercicio.numeroSeries,
                                exercicio.numeroRepeticoes,
                                onClickOk,
                                editarPlanoTreinoViewModel
                            )
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

fun adicionarExercicio(
    indiceDia: Int,
    nomeExercicio: String,
    numeroSeries: Int,
    numeroRepeticoes: Int,
    onClickOk: () -> Unit,
    editarPlanoTreinoViewModel: EditarPlanoTreinoViewModel
) {
    editarPlanoTreinoViewModel.adicionarExercicio(
        indiceDia,
        Exercicio(
            nome = nomeExercicio,
            numeroSeries = numeroSeries,
            numeroRepeticoes = numeroRepeticoes,
            idDiaTreino = -1
        )
    )

    editarPlanoTreinoViewModel.setNomeExercicio(indiceDia, "")
    editarPlanoTreinoViewModel.setNumeroSeries(indiceDia, "")
    editarPlanoTreinoViewModel.setNumeroRepeticoes(indiceDia, "")
    onClickOk()
}