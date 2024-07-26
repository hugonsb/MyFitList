package com.happs.myfitlist.view.dieta

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.navigation.canGoBack
import com.happs.myfitlist.room.RepositoryResponse
import com.happs.myfitlist.state.PlanoAlimentarState
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
import com.happs.myfitlist.viewmodel.dieta.EditarPlanoAlimentarViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditarPlanoAlimentarVIew(
    navController: NavHostController,
    planoAlimentarId: Int,
    editarPlanoDietaViewModel: EditarPlanoAlimentarViewModel = koinViewModel<EditarPlanoAlimentarViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by editarPlanoDietaViewModel.editarPlanoAlimentarState.collectAsState()

    var isNomePlanoDietaError by rememberSaveable { mutableStateOf(false) }

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
                            navController.popBackStack("dieta", false)
                        }
                        openDialog.value = false
                    }
                )
            }

            EditarPlanoAlimentarContent(
                editarPlanoDietaViewModel = editarPlanoDietaViewModel,
                pagerState = pagerState,
                isNomePlanoDietaError = isNomePlanoDietaError,
                enabledButton = enabledButton,
                openDialog = openDialog,
                state = state,
                onNomePlanoChange = { nome ->
                    if (nome.length <= 100) {
                        editarPlanoDietaViewModel.setNomePlanoAlimentar(nome)
                        isNomePlanoDietaError = false
                    }
                },
                onSaveClick = {
                    coroutineScope.launch {
                        enabledButton = false
                        val (success, message) = editarPlanoDietaViewModel.editarPlanoAlimentar(
                            planoAlimentarId
                        )
                        if (success) {
                            navController.popBackStack("dieta", false)
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
fun EditarPlanoAlimentarContent(
    editarPlanoDietaViewModel: EditarPlanoAlimentarViewModel,
    pagerState: PagerState,
    isNomePlanoDietaError: Boolean,
    enabledButton: Boolean,
    openDialog: MutableState<Boolean>,
    state: RepositoryResponse.Success<PlanoAlimentarState>,
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
        }, barTitle = "Editar plano alimentar")

        OutlinedTextField(
            value = state.data.nomePlanoAlimentar,
            onValueChange = onNomePlanoChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = isNomePlanoDietaError,
            supportingText = { if (isNomePlanoDietaError) Text(stringResource(R.string.digite_um_nome_para_o_plano)) },
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
                    key = { pageIndex -> pageIndex }
                ) { currentPage ->
                    CustomCardEditarDiaSemanaDieta(
                        editarPlanoAlimentarViewModel = editarPlanoDietaViewModel,
                        indiceDia = currentPage,
                        state = state
                    )
                }
            }
        }

        FilledTonalButton(
            onClick = onSaveClick,
            enabled = enabledButton,
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
fun CustomCardEditarDiaSemanaDieta(
    editarPlanoAlimentarViewModel: EditarPlanoAlimentarViewModel,
    indiceDia: Int,
    state: RepositoryResponse.Success<PlanoAlimentarState>
) {

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


            state.data.refeicoesList[indiceDia].forEach { refeicao ->
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
                        Column {
                            Text(
                                text = "${refeicao.tipo}:",
                                fontFamily = myFontBody,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyWhite,
                            )
                            Text(
                                text = refeicao.detalhes,
                                fontFamily = myFontBody,
                                fontSize = 15.sp,
                                color = MyWhite,
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    editarPlanoAlimentarViewModel.removerRefeicao(
                                        indiceDia,
                                        refeicao
                                    )
                                },
                            tint = MyWhite,
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = "Remover refeição",
                        )
                    }
                }
            }

            if (openDialog.value) {
                CustomAlertDialogEditarRefeicao(
                    title = "Adicionar Refeição",
                    dia = indiceDia,
                    onClickOk = {
                        openDialog.value = false
                        enabledButton = true
                    },
                    onClickCancelar = {
                        openDialog.value = false
                        enabledButton = true
                    },
                    editarPlanoAlimentarViewModel,
                    state
                )
            }

            OutlinedButton(
                onClick = {
                    enabledButton = false
                    openDialog.value = true
                },
                enabled = enabledButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyRed,
                    disabledContainerColor = MyRed.copy(0.5f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Adicionar refeição",
                    fontFamily = myFontTitle,
                    fontSize = 18.sp,
                    color = MyWhite,
                )
            }

        }
    }
}

@Composable
fun CustomAlertDialogEditarRefeicao(
    title: String,
    dia: Int,
    onClickOk: () -> Unit,
    onClickCancelar: () -> Unit,
    editarPlanoAlimentarViewModel: EditarPlanoAlimentarViewModel,
    state: RepositoryResponse.Success<PlanoAlimentarState>
) {
    var isTipoRefeicaoError by rememberSaveable { mutableStateOf(false) }
    var isDetalhesRefeicaoError by rememberSaveable { mutableStateOf(false) }

    var checkerClone by rememberSaveable { mutableStateOf(false) }

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
                    value = state.data.tipoRefeicao[dia],
                    onValueChange = {
                        if (it.length <= 200) {
                            editarPlanoAlimentarViewModel.setTipoRefeicao(dia, it)
                            isTipoRefeicaoError = false
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = isTipoRefeicaoError,
                    supportingText = { if (isTipoRefeicaoError) Text(stringResource(R.string.campo_obrigat_rio)) },
                    placeholder = {
                        Text(
                            text = "Tipo (Almoço, Jantar, etc)",
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

                OutlinedTextField(
                    value = state.data.detalhesRefeicao[dia],
                    onValueChange = {
                        if (it.length <= 500) {
                            editarPlanoAlimentarViewModel.setDetalhesRefeicao(dia, it)
                            isDetalhesRefeicaoError = false
                        }
                    },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = isDetalhesRefeicaoError,
                    supportingText = { if (isDetalhesRefeicaoError) Text(stringResource(R.string.campo_obrigat_rio)) },
                    maxLines = 8,
                    placeholder = {
                        Text(
                            text = "Detalhes da refeição",
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = checkerClone,
                        onCheckedChange = { checkerClone = !checkerClone },
                        colors = CheckboxDefaults.colors(
                            MaterialTheme.colorScheme.primary,
                            checkmarkColor = MyWhite
                        )
                    )
                    Text(
                        text = "Adicionar essa refeição em todos os outros dias da semana",
                        fontFamily = myFontBody,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedButton(
                    onClick = {
                        val tipoRefeicao = state.data.tipoRefeicao[dia]
                        val detalhesRefeicao = state.data.detalhesRefeicao[dia]

                        if (tipoRefeicao.isNotEmpty() && detalhesRefeicao.isNotEmpty()) {

                            if (checkerClone) {
                                for (i in 0..6) {
                                    adicionarRefeicao(
                                        i,
                                        tipoRefeicao,
                                        detalhesRefeicao,
                                        onClickOk,
                                        editarPlanoAlimentarViewModel
                                    )
                                }
                            } else {
                                adicionarRefeicao(
                                    dia,
                                    tipoRefeicao,
                                    detalhesRefeicao,
                                    onClickOk,
                                    editarPlanoAlimentarViewModel
                                )
                            }

                        } else {
                            if (tipoRefeicao.isEmpty()) {
                                isTipoRefeicaoError = true
                            }
                            if (detalhesRefeicao.isEmpty()) {
                                isDetalhesRefeicaoError = true
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

fun adicionarRefeicao(
    indiceDia: Int,
    tipo: String,
    detalhes: String,
    onClickOk: () -> Unit,
    editarPlanoAlimentarViewModel: EditarPlanoAlimentarViewModel
) {
    editarPlanoAlimentarViewModel.adicionarRefeicao(
        indiceDia,
        Refeicao(
            tipo = tipo,
            detalhes = detalhes,
            idDiaDieta = -1
        )
    )

    editarPlanoAlimentarViewModel.setTipoRefeicao(indiceDia, "")
    editarPlanoAlimentarViewModel.setDetalhesRefeicao(indiceDia, "")
    onClickOk()
}